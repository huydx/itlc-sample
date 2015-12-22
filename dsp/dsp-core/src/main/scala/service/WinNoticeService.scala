package service

import akka.actor.{ Actor, ActorLogging }
import banker.AdvertiserManager
import model.DAO.{ UserDAO, BidPriceDAO, CtrDAO, ImpMemoizationDAO }
import model.entity.WinNotice

class WinNoticeService extends Actor with ActorLogging {
  implicit val ec = context.dispatcher

  def receive = {
    case WinNotice(impid, price, isClick) ⇒
      mainLogic(impid, price, isClick)
      aggerateLogic(impid, isClick)
    case _ ⇒ log.warning(" wint notice service receive unknown message")
  }

  def mainLogic(impid: String, price: Float, isClick: Int) = {
    ImpMemoizationDAO.getBidImpInfo(impid) map {
      case Some(info) ⇒
        val infos = info.split('|')
        println(s"imp memoize infos: advid: ${infos(0)}, siteid: ${infos(1)}, uid: ${infos(2)}")
        val (advid, siteid, uid) = (infos(0), infos(1), infos(2))

        AdvertiserManager.drawBudget(advid.toInt, price / 1000)

        BidPriceDAO.setLastClickWin(advid.toInt, siteid)
        UserDAO.saveUserClickResult(uid, siteid, advid.toInt)
        CtrDAO.incrImp(advid.toInt)
        if (isClick == 1) {
          AdvertiserManager.clickProfitIncrement(advid.toInt)
          CtrDAO.incrClick(advid.toInt)
        }
      case None ⇒ log.error(s"$impid is not found in memcached")
    }
  }

  def aggerateLogic(impid: String, isClick: Int) = {
    ImpMemoizationDAO.getBidImpInfo2(impid) map {
      case Some(info) ⇒
        val infos = info.split('|')
        val (site, devicetype, deviceua, advid) = (infos(0), infos(1), infos(2), infos(3))
        println(s"aggregate logic going to increase $site, $devicetype, $deviceua, $advid")
        CtrDAO.incrClick2(site, devicetype, deviceua, advid)
        if (isClick == 1) {
          CtrDAO.incrImp2(site, devicetype, deviceua, advid)
        }

      case None ⇒ log.error(s"$impid is not found in memcached")
    }
  }
}
