package model.DAO

import shade.memcached._
import concurrent.duration._
import scala.util.{ Failure, Success }
import scala.concurrent.ExecutionContext.Implicits.{ global ⇒ ec }

object MemcachedProvider {
  lazy val temporaryTimeout = 3 minutes
  lazy val memcached =
    Memcached(Configuration("127.0.0.1:11211"), ec)

  def errorHandler: (scala.util.Try[Any]) ⇒ Any = {
    case Success(res) ⇒
    case Failure(ex) ⇒ ex.printStackTrace()
  }
}
