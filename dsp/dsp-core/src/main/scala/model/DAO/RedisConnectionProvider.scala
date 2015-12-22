package model.DAO

import com.redis._

object RedisConnectionProvider {
  lazy val clients = new RedisClientPool("localhost", 6379)
}
