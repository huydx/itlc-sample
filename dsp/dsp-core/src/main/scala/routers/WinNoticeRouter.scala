package routers

import akka.actor.{ ActorSystem, Actor }
import model.entity._
import spray.http.StatusCodes
import spray.routing.HttpService
import util.{ ActorFinderProvider, ActorSystemProvider }
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller

trait WinNoticeRouter extends HttpService
    with Actor
    with ActorSystemProvider
    with ActorFinderProvider {

  import WinNoticeJsonFormat._

  val winnotice = {
    post {
      path("winnotice") {
        entity(as[WinNotice]) { winnotice ⇒
          winNoticeActor ! winnotice
          complete(StatusCodes.NoContent)
        }
      }
    }
  }
}
