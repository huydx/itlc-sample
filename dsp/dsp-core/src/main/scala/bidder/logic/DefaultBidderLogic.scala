package bidder.logic

import banker.AdvertiserManager
import model.DAO.{ CtrDAO, ImpMemoizationDAO }
import model.entity._
import util.BidIdGenerator
import scalaz._
import scalaz.Scalaz._

object DefaultBidderLogic extends BidIdGenerator {

  type AdvWithPrice = (Advertiser, Float)

  def bid(implicit req: BidRequest): Option[BidResponse] = {
    val bidid = "default-bidder-tracker"
    val bids = req.imp.map(bidWithImp)
    if (bids.isEmpty) {
      None
    } else {
      BidResponse(req.id, List(SeatBid(bids, "seat")), bidid.some, "JPY".some).some
    }
  }

  private[bidder] def bidWithImp(imp: Imp)(implicit req: BidRequest): Bid = {
    val advs = AdvertiserManager.getListAllAdvertiser //each adv with each seat
    val filteredAdvs = AdvertiserManager.filterWithSite(advs, req.site)

    val advWithCtr = filteredAdvs.map { adv ⇒
      val maybeCtr = getBatchCtr(adv, req) map { ctr ⇒
        val realtimeCtr = getRealtimeCtr(adv, req)
        println(s"realtime ctr is $realtimeCtr")
        println(s"batch ctr is $ctr")
        if (realtimeCtr > 0) {
          (realtimeCtr + ctr) / 2
        } else {
          ctr
        }
      }
      //バブル対策で値段固定
      (adv, maybeCtr)
    } map { some ⇒
      some._2 match {
        case Some(c: Float) ⇒ (some._1, c)
        case None ⇒ (some._1, 0f)
      }
    }
    val maxAdvWithCtr = advWithCtr.maxBy(_._2)
    val price = pricePicker(req.user, req.site, maxAdvWithCtr._1, imp, Some(maxAdvWithCtr._2))
    val bid = Bid(generateBidId,
      imp.id,
      price,
      List(maxAdvWithCtr._1.id.toString),
      "http://ec2-54-178-130-132.ap-northeast-1.compute.amazonaws.com:9090/winnotice")
    ImpMemoizationDAO.addTempBiddedInfo(bid, req.site, maxAdvWithCtr._1.id, req.user)
    ImpMemoizationDAO.addTempBiddedInfo2(bid, req, maxAdvWithCtr._1.id)
    bid
  }

  private[bidder] def getBatchCtr(adv: Advertiser, req: BidRequest): Option[Float] = {
    //s"$site|$deviceType|$deviceUa|$advid"
    val ret: Option[Option[Float]] = for {
      site ← req.site
      device ← req.device
      deviceType = device.devicetype
      ua = device.ua
    } yield {
      val advid = adv.id
      CtrDAO.getCtrFromBatch(site.id.toString, ua, advid)
    }
    ret.flatMap(identity)
  }

  private[bidder] def getRealtimeCtr(adv: Advertiser, req: BidRequest): Float = {
    val maybeCtr = for {
      site ← req.site
      user ← req.user
      device ← req.device
    } yield {
      CtrDAO.getCtr2(site.page, device.devicetype.toString, device.ua, adv.id.toString)
    }
    maybeCtr match {
      case Some(ctr) ⇒ ctr
      case None ⇒ 0
    }
  }

  private[bidder] def pricePicker(maybeUser: Option[User],
    maybeSite: Option[Site],
    adv: Advertiser, imp: Imp, ctr: Option[Float]): Float = {

    import TacticOps._
    if (AdvertiserManager.checkBudget(adv.id) < 0) return 0
    else {
      maybeUser match {
        case Some(user) ⇒
          if (UserTypeExtractor.isClickedUser(user, maybeSite, adv.id)) {
            bidBaseOnUser(ClickedUser(user.id), adv, imp, maybeSite, ctr)
          } else if (UserTypeExtractor.isRecentVisitUser(user, maybeSite, adv.id)) {
            bidBaseOnUser(RecentVisitUser(user.id), adv, imp, maybeSite, ctr)
          } else if (UserTypeExtractor.isVisistedUser(user)) {
            bidBaseOnUser(VisistedUser(user.id), adv, imp, maybeSite, ctr)
          } else {
            bidBaseOnUser(NewUser(user.id), adv, imp, maybeSite, ctr)
          }
        case None ⇒ bidBaseOnUser(AnynomousUser(), adv, imp, maybeSite, ctr)
      }
    }
  }

  private[bidder] def bidBaseOnUser[A <: UserBase](user: A,
    adv: Advertiser,
    imp: Imp,
    site: Option[Site],
    ctr: Option[Float])(implicit priceTactic: PriceTactic[A]): Float = {

    priceTactic.pickPriceAndSave(user, imp, adv, site, ctr)
  }

  private[bidder] def fixPriceBid(advid: Int): Float = {
    advid match {
      case 0 ⇒ 0
      case 1 ⇒ 50
      case 2 ⇒ 130
      case 3 ⇒ 80
      case 4 ⇒ 130
    }
  }

}

