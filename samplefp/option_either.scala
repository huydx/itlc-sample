val y: String = "z3"

val x: Option[String] = y match {
  case "z" => Some("z2")
  case _ => None
}

x match {
  case Some(t) => println(t)
  case None => println("none")
}
