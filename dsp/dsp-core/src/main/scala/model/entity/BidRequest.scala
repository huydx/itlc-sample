package model.entity

import spray.json._

//{"body":
//  {"id":"req-0",
//   "imp":[{"id":"imp-0","bidfloor":0.005,"bidfloorcur":"USD"}],
//   "site":{"id":"GZCVlVEklesTBofbJXgXAqDZQ.jp","name":"VwTKwNYArwoi","page":"http://GZCVlVEklesTBofbJXgXAqDZQ.jp/umhFBGeiPHocWSMBdf"},
//   "device":{"ua":"iOS","devicetype":1},
//   "user":{"id":"user_1"},
//   "test":1,"at":2,
//   "tmax":1000},
//   "result":[false,false,false,false,false] //result will be pass to post auction service
//  }
// }

sealed trait BidRequestBase

case class ReceiveBidRequest(body: BidRequest, result: Seq[Boolean])

case class BidRequest(id: String, imp: Seq[Imp], site: Option[Site],
  device: Option[Device], user: Option[User],
  test: Option[Int], at: Option[Int],
  max: Option[Int]) extends BidRequestBase

case class MobileBidReuquest(id: String, imp: Seq[Imp], site: Option[Site], user: User)

case class Imp(id: String, bidfloor: Float, bidfloorcur: String)
case class Device(ua: String, devicetype: Int)
case class Site(id: String, name: String, page: String)

sealed trait UserBase
case class User(id: String) extends UserBase {
  def isVisisted: Boolean = true
  def isAnorNymous: Boolean = true
}

case class AnynomousUser() extends UserBase
case class NewUser(id: String) extends UserBase
case class VisistedUser(id: String) extends UserBase
case class ClickedUser(id: String) extends UserBase
case class RecentVisitUser(id: String) extends UserBase

object BidRequestJsonProtocol extends DefaultJsonProtocol {
  implicit val impFormat = jsonFormat3(Imp)
  implicit val deviceFormat = jsonFormat2(Device)
  implicit val siteFormat = jsonFormat3(Site)
  implicit val userFormat = jsonFormat1(User)
  implicit val bidrequestFormat = jsonFormat8(BidRequest)
  implicit val receiveBidReqFormat = jsonFormat2(ReceiveBidRequest)
}

