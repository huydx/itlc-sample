package model.DAO

/*
  redis データモデル
  ctr record : {ctr:advid:siteid} list[imp num, click num]
 */

object CtrDAO {
  lazy val redis = RedisConnectionProvider.clients
  lazy val ctrImpPrefix = "ctr|imp"
  lazy val ctrClickPrefix = "ctr|click"

  def incrImp(advid: Int) = {
    redis.withClient { client ⇒
      val kimp = s"$ctrImpPrefix|$advid"
      client.incr(kimp)
    }
  }

  def incrClick(advid: Int) = {
    redis.withClient { client ⇒
      val kclick = s"$ctrClickPrefix|$advid"
      client.incr(kclick)
    }
  }

  def incrImp2(page: String, devicetype: String, deviceua: String, advid: String) = {
    redis.withClient { client ⇒
      val imp = s"$ctrImpPrefix|$page|$devicetype|$deviceua|$advid"
      client.incr(imp)
    }
  }

  def incrClick2(page: String, devicetype: String, deviceua: String, advid: String) = {
    redis.withClient { client ⇒
      val click = s"$ctrClickPrefix|$page|$devicetype|$deviceua|$advid"
      client.incr(click)
    }
  }

  def getCtr2(page: String, devicetype: String, deviceua: String, advid: String): Float = {
    redis.withClient { client ⇒
      val imp = s"$ctrImpPrefix|$page|$devicetype|$deviceua|$advid"
      val click = s"$ctrClickPrefix|$page|$devicetype|$deviceua|$advid"
      val maybeCtr: Option[Float] = for {
        impVal ← client.get[String](imp)
        clickVal ← client.get[String](click)
      } yield {
        if (impVal.toFloat < 5f) 0
        else impVal.toFloat / clickVal.toFloat
      }
      println(s"maybe ctr is $maybeCtr")

      maybeCtr match {
        case Some(ctr) ⇒ ctr
        case None ⇒ 0
      }
    }
  }

  def getCtr(advid: Int): Float = {
    redis.withClient { client ⇒
      val kimp = s"$ctrImpPrefix|$advid"
      val kclick = s"$ctrClickPrefix|$advid"

      val maybeCtr: Option[Float] = for {
        imp ← client.get[String](kimp)
        click ← client.get[String](kclick)
      } yield {
        click.toFloat / imp.toFloat
      }

      maybeCtr match {
        case Some(ctr) ⇒ ctr
        case None ⇒ 0
      }
    }
  }

  def getCtrFromBatch(site: String, deviceUa: String, advid: Int): Option[Float] = {
    val key = s"$site|$deviceUa|$advid"
    redis.withClient { client ⇒
      client.get(key).map(_.toFloat)
    }
  }
}
