package bidder.logic

import banker.AdvertiserManager
import model.DAO.BidPriceDAO
import model.entity._

trait PriceTactic[A <: UserBase] {
  def pickPrice(user: A, imp: Imp, adv: Advertiser, site: Option[Site], ctr: Option[Float]): Float
  def pickPriceAndSave(user: A, imp: Imp, adv: Advertiser, site: Option[Site], ctr: Option[Float]) = {
    val price = pickPrice(user, imp, adv, site, ctr)
    updateNewPrice(adv, site, price, ctr)
    price * 1000 //CPM計算
  }

  def basePrice(imp: Imp, adv: Advertiser, maybeSite: Option[Site], ctrMaybe: Option[Float]): Float = {
    val maybePrice =
      for {
        site ← maybeSite
        price ← BidPriceDAO.getLastBidPrice(adv.id, site.id)
        ctr ← ctrMaybe
      } yield {
        val cpc = adv.cpc
        val price = cpc * ctr * (1 / AdvertiserManager.getDampingFactor(adv.id))
        if (price < imp.bidfloor) {
          throw new RuntimeException("no bid")
        } else {
          price
        }
      }

    maybePrice match {
      case Some(price) ⇒ price
      case None ⇒ imp.bidfloor
    }
  }

  def updateNewPrice(adv: Advertiser, maybeSite: Option[Site], newPrice: Float, ctr: Option[Float]) = {
    for {
      site ← maybeSite
    } yield {
      BidPriceDAO.setBidPrice(adv.id, site.id, newPrice)
      BidPriceDAO.removeLastClickWin(adv.id, site.id)
    }
  }

}

object TacticOps {

  implicit object PriceTacticForAnyUser extends PriceTactic[AnynomousUser] {
    override def pickPrice(user: AnynomousUser, imp: Imp, adv: Advertiser, site: Option[Site], ctr: Option[Float]): Float = {
      println("price for any user")
      basePrice(imp, adv, site, ctr)
    }
  }

  implicit object PriceTacticForNewUser extends PriceTactic[NewUser] {
    def pickPrice(user: NewUser, imp: Imp, adv: Advertiser, site: Option[Site], ctr: Option[Float]): Float = {
      println("price for new user")
      basePrice(imp, adv, site, ctr)
    }
  }

  implicit object PriceTacticForExistUser extends PriceTactic[VisistedUser] {
    def pickPrice(user: VisistedUser, imp: Imp, adv: Advertiser, site: Option[Site], ctr: Option[Float]): Float = {
      println("price for visited user")
      basePrice(imp, adv, site, ctr) + DspConfig.marginForExistUser
    }
  }

  implicit object PriceTacticForClickedUser extends PriceTactic[ClickedUser] {
    def pickPrice(user: ClickedUser, imp: Imp, adv: Advertiser, site: Option[Site], ctr: Option[Float]): Float = {
      println("price for clicked user")
      basePrice(imp, adv, site, ctr) + DspConfig.marginForClickedUser
    }
  }

  implicit object PriceTacticForRecentVisit extends PriceTactic[RecentVisitUser] {
    def pickPrice(user: RecentVisitUser, imp: Imp, adv: Advertiser, site: Option[Site], ctr: Option[Float]): Float = {
      println("price for recent visit user")
      basePrice(imp, adv, site, ctr) + DspConfig.marginForRecentVisitUser
    }
  }

}
