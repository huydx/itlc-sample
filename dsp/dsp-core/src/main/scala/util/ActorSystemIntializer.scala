package util

import akka.actor.{ Actor, ActorSystem }
import akka.util.Timeout

import scala.concurrent.duration._

trait ActorSystemBase {
  //everything should be under 50 millisec!!
  implicit val timeOut = Timeout(50 milliseconds)
  val bidderActorName = "bidder-actor"
  val postAuctionActorName = "post-auction-actor"
  val winNoticeActorName = "win-notice-actor"
}

trait ActorSystemProvider extends Actor {
  implicit def actorSystem: ActorSystem = context.system
  implicit def ec = actorSystem.dispatcher
}

trait ActorFinderProvider extends ActorSystemBase {
  def bidActor(implicit actorSystem: ActorSystem) =
    actorSystem.actorSelection(s"/user/$bidderActorName")
  def postAuctionActor(implicit actorSystem: ActorSystem) =
    actorSystem.actorSelection(s"/user/$postAuctionActorName")
  def winNoticeActor(implicit actorSystem: ActorSystem) =
    actorSystem.actorSelection(s"/user/$winNoticeActorName")
}
