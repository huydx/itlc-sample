package util

trait BidIdGenerator {
  def generateBidId: String = {
    s"bid-${java.util.UUID.randomUUID.toString}"
  }
}
