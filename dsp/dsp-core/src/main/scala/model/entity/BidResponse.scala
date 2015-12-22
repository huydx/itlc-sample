package model.entity

import spray.json._

case class BidResponse(id: String, seatbid: Seq[SeatBid],
  bidid: Option[String], cur: Option[String])
case class SeatBid(bid: Seq[Bid], seat: String)
case class Bid(id: String, impid: String,
    price: Float, adomain: Seq[String],
    nurl: String) extends Ordered[Bid] {
  override def compare(that: Bid): Int = {
    this.price compare that.price
  }
}

object BidResponseJsonProtocol extends DefaultJsonProtocol {
  implicit val bidFormat = jsonFormat5(Bid)
  implicit val seadbidFormat = jsonFormat2(SeatBid)
  implicit val bidresponseFormat = jsonFormat4(BidResponse)
}
