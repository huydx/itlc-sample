package routers

import akka.actor.Actor
import banker.AdvertiserManager
import model.DAO.CtrDAO
import model.entity.DspConfig
import spray.routing.HttpService
import util.{ ActorFinderProvider, ActorSystemProvider }

trait ConfigRouter extends HttpService
    with Actor
    with ActorSystemProvider
    with ActorFinderProvider {

  val configroute = {
    get {
      path("priceforclick" / IntNumber) { (price) ⇒
        DspConfig.marginForClickedUser = price.toFloat
        complete(s"price is set to ${DspConfig.marginForClickedUser}")
      } ~ path("priceforvisit" / IntNumber) { (price) ⇒
        DspConfig.marginForExistUser = price.toFloat
        complete(s"price is set to ${DspConfig.marginForExistUser}")
      } ~ path("priceforrecent" / IntNumber) { (price) ⇒
        DspConfig.marginForRecentVisitUser = price.toFloat
        complete(s"price is set to ${DspConfig.marginForRecentVisitUser}")
      } ~ path("dampingadv0" / IntNumber) { (dampval) ⇒
        DspConfig.dampingFactorForAdv0 = DspConfig.dampingFactorForAdv0 + calculateDamping(dampval) / 10f
        complete(s"damping factor for adv0 is set to ${DspConfig.dampingFactorForAdv0}")
      } ~ path("dampingadv1" / IntNumber) { (dampval) ⇒
        DspConfig.dampingFactorForAdv1 = DspConfig.dampingFactorForAdv1 + calculateDamping(dampval) / 10f
        complete(s"damping factor for adv1 is set to ${DspConfig.dampingFactorForAdv1}")
      } ~ path("dampingadv2" / IntNumber) { (dampval) ⇒
        DspConfig.dampingFactorForAdv2 = DspConfig.dampingFactorForAdv2 + calculateDamping(dampval) / 10f
        complete(s"damping factor for adv2 is set to ${DspConfig.dampingFactorForAdv2}")
      } ~ path("dampingadv3" / IntNumber) { (dampval) ⇒
        DspConfig.dampingFactorForAdv3 = DspConfig.dampingFactorForAdv3 + calculateDamping(dampval) / 10f
        complete(s"damping factor for adv3 is set to ${DspConfig.dampingFactorForAdv3}")
      } ~ path("dampingadv4" / IntNumber) { (dampval) ⇒
        DspConfig.dampingFactorForAdv4 = DspConfig.dampingFactorForAdv4 + calculateDamping(dampval) / 10f
        complete(s"damping factor for adv4 is set to ${DspConfig.dampingFactorForAdv4}")
      } ~ path("budgetinfo") {
        complete(budgetInfoGet)
      } ~ path("margininfo") {
        complete(marginInfoGet)
      }
    }
  }

  private[this] def calculateDamping(d: Int): Int = {
    if (d > 10) {
      10 - d
    } else d
  }

  private[this] def budgetInfoGet: String = {
    (0 to 4).foldLeft("") { (str, advid) ⇒
      val dampfactor = AdvertiserManager.getDampingFactor(advid)
      str + s"advid : $advid, budget: ${AdvertiserManager.checkBudget(advid)}, damping: $dampfactor, ctr: ${CtrDAO.getCtr(advid)}\n"
    } + s"\n profit : ${AdvertiserManager.profit}"
  }

  private[this] def marginInfoGet: String = {
    s"""
      |margin for click user  : ${DspConfig.marginForClickedUser}
      |margin for visit user  : ${DspConfig.marginForExistUser}
      |margin for recent user : ${DspConfig.marginForRecentVisitUser}
    """.stripMargin
  }

}
