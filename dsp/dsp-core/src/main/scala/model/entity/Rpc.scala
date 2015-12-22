package model.entity

object Rpc {
  case class SaveAuctionResult(user: User)
  case class SaveUserClickResult(user: User, site: Site, advid: Int)
  case class UpdateAdvertiserAveragePrice(user: User, site: Site, advid: Int)
  case class UpdateBudget(advid: Int, price: Float)
}
