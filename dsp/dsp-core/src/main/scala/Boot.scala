import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import service.{ WinNoticeService, BidderService, PostAuctionService, RouterService }
import spray.can.Http
import util.ActorSystemBase

object Boot extends ActorSystemBase {
  def main(args: Array[String]) = {
    val actorSystem = ActorSystem("hdsp")
    val service = actorSystem.actorOf(Props[RouterService], "bid-service")

    //create actor
    actorSystem.actorOf(Props[BidderService], bidderActorName)
    actorSystem.actorOf(Props[PostAuctionService], postAuctionActorName)
    actorSystem.actorOf(Props[WinNoticeService], winNoticeActorName)

    IO(Http)(actorSystem) ! Http.Bind(
      listener = service,
      interface = "0.0.0.0",
      port = 9090
    )
  }
}
