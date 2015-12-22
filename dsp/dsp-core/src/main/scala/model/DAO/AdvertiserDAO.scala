package model.DAO

import model.entity._
import scalikejdbc._

/**
 *  table1: advertisers: id: Int, url: String, landing_page: String, name: String
 *  table2: advertiser_block: id: Int, advertiser_id: Int, url: String
 */

object AdvertiserDAO extends DbConnectionProvider {
  def getAll: Seq[Advertiser] = DB readOnly { implicit session ⇒
    sql"select * from advertisers".map(rs ⇒
      Advertiser(rs.int("id"),
        rs.string("url"),
        rs.string("landing_page"),
        rs.string("name"),
        rs.bigDecimal("budget"),
        rs.float("cpc"),
        getBlockListFrom(rs.int("id")))
    ).toList().apply()
  }

  def getBlockListFrom(advId: Int): Seq[String] = DB readOnly { implicit session ⇒
    sql"select * from advertiser_block where advertiser_id = $advId".map { rs ⇒
      rs.string("url")
    }.toList().apply()
  }
}