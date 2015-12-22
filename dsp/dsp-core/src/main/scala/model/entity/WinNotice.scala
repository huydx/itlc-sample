package model.entity

import spray.json.DefaultJsonProtocol

case class WinNotice(impid: String, price: Float, is_click: Int)

object WinNoticeJsonFormat extends DefaultJsonProtocol {
  implicit val winnoticeFormat = jsonFormat3(WinNotice)
}
