package service

import akka.actor.{ ActorLogging, Actor }
import model.entity.Rpc._
import model.DAO.{ UserDAO ⇒ UserDb, CtrDAO }

class PostAuctionService extends Actor with ActorLogging {
  def receive = {
    case SaveAuctionResult(user) ⇒ UserDb.saveUser(user)
    case _ ⇒ log.warning("post auction service receive unknown message")
  }
}
