package routers

import akka.actor.{ Actor, ActorSystem }
import akka.pattern._
import model.entity._
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
import spray.httpx.marshalling._
import spray.routing.HttpService
import util.{ ActorFinderProvider, ActorSystemProvider }

import scala.util.{ Failure, Success }

trait BidReqRouter extends HttpService
    with Actor
    with ActorSystemProvider
    with ActorFinderProvider {

  import BidResponseJsonProtocol._
  import BidRequestJsonProtocol._

  val bidreq = {
    post {
      path("bidrequest") {
        entity(as[BidRequest]) { req ⇒
          onComplete((bidActor ask req).mapTo[Option[BidResponse]]) {
            case Success(res) ⇒
              res match {
                case Some(response) ⇒ complete(marshal(response))
                case None ⇒ complete(StatusCodes.NoContent)
              }
            case Failure(ex) ⇒ {
              ex.printStackTrace()
              complete(StatusCodes.NoContent)
            }
          }
        }
      }
    }
  }
}
