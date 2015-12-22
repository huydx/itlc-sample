package model.entity

object DspConfig {
  @volatile var marginForExistUser = 30.0f
  @volatile var marginForClickedUser = 10.0f
  @volatile var marginForRecentVisitUser = 50.0f
  @volatile var dampingFactorForAdv0 = 1.0f
  @volatile var dampingFactorForAdv1 = 1.0f
  @volatile var dampingFactorForAdv2 = 1.0f
  @volatile var dampingFactorForAdv3 = 1.0f
  @volatile var dampingFactorForAdv4 = 1.0f
}
