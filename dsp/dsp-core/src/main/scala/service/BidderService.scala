package service

import akka.actor.{ Actor, ActorLogging }
import bidder.BidderProcessor
import model.entity.BidRequest

class BidderService extends Actor with ActorLogging {
  implicit val system = context.system
  def receive = {
    case req: BidRequest ⇒
      val response = BidderProcessor.execute(req)
      sender ! response
    case mes ⇒ log.warning(s"bidder actor receive unknown message: $mes")
  }
}
