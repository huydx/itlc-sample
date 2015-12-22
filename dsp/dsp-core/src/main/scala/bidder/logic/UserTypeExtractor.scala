package bidder.logic

import model.entity._
import model.DAO._

object UserTypeExtractor {
  def isVisistedUser(user: User): Boolean = {
    UserDAO.checkUserExist(user)
  }

  def isRecentVisitUser(user: User, siteOpt: Option[Site], advId: Int): Boolean = {
    val result = for {
      site ← siteOpt
      recency ← UserDAO.getUserRecencyHour(user, site, advId)
    } yield {
      recency < 24
    }

    result match {
      case Some(res) ⇒ res
      case None ⇒ false
    }
  }

  def isClickedUser(user: User, siteOpt: Option[Site], advid: Int): Boolean = {
    val result = for {
      site ← siteOpt
    } yield {
      UserDAO.checkUserClicked(user, site, advid)
    }

    result match {
      case Some(res) ⇒ res
      case None ⇒ false
    }
  }
}
