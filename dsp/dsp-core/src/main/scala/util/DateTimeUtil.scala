package util

import java.text.SimpleDateFormat

object DateTimeUtil {
  lazy val dateFormat = new SimpleDateFormat("yyyy/MM/dd|HH:mm:ss.SSS")
  def now(): Long = {
    System.currentTimeMillis()
  }

  def nowInString(): String = {
    System.currentTimeMillis().toString
  }

  def stringToTime(in: String): Long = {
    in.toLong
  }

  def recency(in: String): Long = {
    System.currentTimeMillis() - in.toLong
  }

  def recencyInHour(in: String): Int = {
    (recency(in) / (1000 * 60 * 60)).asInstanceOf[Int]
  }
}
