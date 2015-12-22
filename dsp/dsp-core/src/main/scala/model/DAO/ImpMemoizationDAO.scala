package model.DAO

//Impの短期的な情報をmemcachedの中に保存
import model.entity._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.{ global ⇒ ec }

object ImpMemoizationDAO {
  val memcached = MemcachedProvider.memcached
  val timeout = MemcachedProvider.temporaryTimeout

  def addTempBiddedInfo(bid: Bid, siteOpt: Option[Site], advid: Int, userOpt: Option[User]) = {
    for {
      site ← siteOpt
      user ← userOpt
    } yield {
      val k = s"${bid.impid}"
      val v = s"$advid|${site.id}|${user.id}"
      memcached.set[String](k, v, timeout) onComplete MemcachedProvider.errorHandler
    }
  }

  def addTempBiddedInfo2(bid: Bid, bidreq: BidRequest, advid: Int) = {
    for {
      site ← bidreq.site
      user ← bidreq.user
      device ← bidreq.device
    } yield {
      val k = s"2|${bid.impid}"
      val v = s"${site.page}|${device.devicetype}|${device.ua}|$advid|"
      memcached.set[String](k, v, timeout) onComplete MemcachedProvider.errorHandler
    }
  }

  def getBidImpInfo(impId: String): Future[Option[String]] = {
    val k = s"$impId"
    memcached.get[String](k)
  }

  def getBidImpInfo2(impId: String): Future[Option[String]] = {
    val k = s"2|$impId"
    memcached.get[String](k)
  }
}
