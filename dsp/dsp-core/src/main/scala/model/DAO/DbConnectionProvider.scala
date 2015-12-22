package model.DAO

import scalikejdbc._

trait DbConnectionProvider {
  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.singleton("jdbc:mysql://localhost:3306/hdsp", "root", "")
}
