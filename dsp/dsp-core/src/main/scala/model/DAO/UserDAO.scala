package model.DAO

import model.entity._
import util.{ DateTimeUtil ⇒ time }

/*
  redis データモデル：
  user record: {user:id} : [{access1, access2, ...}]
  user click record: {userid:site:advid}:[{clicktime1, clicktime2,.. }]
 */

object UserDAO {
  lazy val redis = RedisConnectionProvider.clients
  lazy val userKeyPrefix = "user"

  def saveUser(user: User) = {
    redis.withClient { client ⇒
      val k = s"$userKeyPrefix:${user.id}"
      val v = time.now()
      client.lpush(k, v)
    }
  }

  def getUser(user: User): Option[Seq[Option[String]]] = {
    redis.withClient { client ⇒
      val k = s"$userKeyPrefix:${user.id}"
      client.lrange(k, 0, -1)
    }
  }

  def getUserLastAccess(user: User): Option[String] = {
    redis.withClient { client ⇒
      val k = s"$userKeyPrefix:${user.id}"
      client.lindex(k, -1)
    }
  }

  def checkUserExist(user: User): Boolean = {
    redis.withClient { client ⇒
      val k = s"$userKeyPrefix:${user.id}"
      client.exists(k)
    }
  }

  def saveUserClickResult(uid: String, siteid: String, advid: Int) = {
    redis.withClient { client ⇒
      val k = s"$uid|$siteid|$advid"
      println(s" key for user clicked is going to be saved: $k")

      val v = time.now()
      client.lpush(k, v)
    }
  }

  def getUserClickResult(user: User, site: Site, advid: Int): Option[Seq[Option[String]]] = {
    redis.withClient { client ⇒
      val k = s"${user.id}|${site.id}|$advid"
      client.lrange(k, 0, -1)
    }
  }

  def checkUserClicked(user: User, site: Site, advid: Int): Boolean = {
    redis.withClient { client ⇒
      val k = s"${user.id}|${site.id}|$advid"
      println(s"key for check user clicked is $k}")
      client.exists(k)
    }
  }

  def getUserLastClick(user: User, site: Site, advid: Int): Option[String] = {
    redis.withClient { client ⇒
      val k = s"${user.id}|${site.id}|$advid"
      client.lindex(k, -1)
    }
  }

  def getUserRecencyHour(user: User, site: Site, advid: Int): Option[Int] = {
    getUserLastClick(user, site, advid).map { click ⇒ time.recencyInHour(click) }
  }
}
