package model.DAO

/*
  Bidの平均価格を保存する
  bid price record: {price-advid|siteid} price
  bid last_click_win record: {last_click_win-advid|siteid} price
 */

object BidPriceDAO {
  lazy val redis = RedisConnectionProvider.clients
  lazy val pricePrefix = "price"
  lazy val lastClickWinPrefix = "last-click-win"

  def setBidPrice(advid: Int, siteid: String, price: Float) = {
    redis.withClient { client ⇒
      val k = s"$pricePrefix|$advid|$siteid"
      val v = price
      client.set(k, v)
    }
  }

  def getLastBidPrice(advid: Int, siteid: String): Option[String] = {
    redis.withClient { client ⇒
      val k = s"$pricePrefix|$advid|$siteid"
      client.get[String](k)
    }
  }

  def removeLastClickWin(advid: Int, siteid: String) = {
    redis.withClient { client ⇒
      val k = s"$lastClickWinPrefix|$advid|$siteid"
      client.del(k)
    }
  }

  def setLastClickWin(advid: Int, siteid: String) = {
    redis.withClient { client ⇒
      val k = s"$lastClickWinPrefix|$advid|$siteid"
      val v = true
      client.set(k, v)
    }
  }

  //use option for able to use flatmap
  def checkLastClickWin(advid: Int, siteid: String): Boolean = {
    redis.withClient { client ⇒
      val k = s"$lastClickWinPrefix|$advid|$siteid"
      client.exists(k)
    }
  }
}
