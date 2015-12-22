package service

import akka.actor.Actor
import routers.{ ConfigRouter, BidReqRouter, WinNoticeRouter }

class RouterService extends Actor
    with BidReqRouter
    with WinNoticeRouter
    with ConfigRouter {
  implicit def actorRefFactory = context
  def receive = runRoute(
    winnotice ~
      bidreq ~
      configroute
  )

}
