package bidder.validators

import com.wix.accord._
import model.entity.BidRequest

object BidRequestValidator {
  import dsl._
  implicit val bidRequestValidator = validator[BidRequest] { bidreq ⇒
    bidreq.imp is notEmpty
  }

  def apply(req: BidRequest) = {
    validate(req)
  }
}
