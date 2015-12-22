package banker

import model.DAO.AdvertiserDAO
import model.entity.{ DspConfig, Site, Advertiser }

object AdvertiserManager {
  lazy val getListAllAdvertiser: Seq[Advertiser] = AdvertiserDAO.getAll
  @volatile var profit = 0.0

  def clickProfitIncrement(advid: Int) = {
    getListAllAdvertiser.find(_.id == advid).foreach { foundAdv ⇒
      profit = profit + foundAdv.cpc
    }
  }

  def drawBudget(advid: Int, price: Float) = {
    getListAllAdvertiser.find(_.id == advid).foreach { foundAdv ⇒
      foundAdv.downBudgetBy(price)
    }
  }

  def getDampingFactor(advid: Int): Float = {
    advid match {
      case 0 ⇒ DspConfig.dampingFactorForAdv0
      case 1 ⇒ DspConfig.dampingFactorForAdv1
      case 2 ⇒ DspConfig.dampingFactorForAdv2
      case 3 ⇒ DspConfig.dampingFactorForAdv3
      case 4 ⇒ DspConfig.dampingFactorForAdv4
    }
  }

  def checkBudget(advid: Int): Float = {
    getListAllAdvertiser.find(_.id == advid).head.getBudget
  }

  def filterWithSite(advs: Seq[Advertiser], site: Option[Site]): Seq[Advertiser] = {
    advs.filter { adv ⇒
      site match {
        case None ⇒ true
        case Some(s) ⇒ !adv.blockList.contains(s.id)
      }
    }
  }

}
