package model.entity

import exceptions.BidException

object Advertiser {

  def apply(id: Int,
    url: String,
    landingPage: String,
    name: String,
    budget: BigDecimal,
    cpc: Float,
    blockList: Seq[String]): Advertiser = {
    val adv = new Advertiser(id, url, landingPage, name, cpc, blockList)
    adv.setBudget(budget) // using Builder pattern can resolve this
    adv
  }

}

class Advertiser(val id: Int,
    val url: String,
    val landingPage: String,
    val name: String,
    val cpc: Float,
    val blockList: Seq[String]) {

  @volatile private[this] var budget: BigDecimal = 0
  def setBudget(_budget: BigDecimal) = {
    budget = _budget
  }

  def getBudget: Float = {
    budget.toFloat
  }

  def isOutOfBudget: Boolean = {
    budget <= 0
  }

  def downBudgetBy(price: Float) = {
    val newBudgetFloat = budget.toFloat - price
    if (newBudgetFloat < 0) {
      throw new BidException("out of budget")
    }
    budget = BigDecimal(newBudgetFloat)
  }

}
