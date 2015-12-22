package bidder

import akka.actor.ActorSystem
import bidder.filter.DomainFilter
import bidder.logic.DefaultBidderLogic
import model.entity.Rpc.{ SaveAuctionResult, SaveUserClickResult }
import model.entity._
import util.ActorFinderProvider

object BidderProcessor extends ActorFinderProvider {
  def execute(request: BidRequest)(implicit actorSystem: ActorSystem): Option[BidResponse] = {
    for {
      req ← DomainFilter.filter(request)
      res ← DefaultBidderLogic.bid(req)
    } yield {
      log(request)
      res
    }
  }

  def log(request: BidRequest)(implicit actorSystem: ActorSystem) = {
    for { u ← request.user } yield {
      postAuctionActor ! SaveAuctionResult(u)
    }
  }
}
