package bidder.filter

import model.entity.BidRequest

trait Filter {
  def filter(in: BidRequest): Option[BidRequest]
}

object DomainFilter extends Filter {
  override def filter(in: BidRequest) = {
    Some(in)
  }
}
