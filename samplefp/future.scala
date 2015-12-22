import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Success, Failure}
import scala.util.Try

def foo(i: Int): Future[Int] = Future {
  println("start foo")
  Thread.sleep(1000)
  println("end foo")
  100
}

def bar(i: Int): Future[Int] = Future {
  println("start bar")
  Thread.sleep(2000)
  println("end bar")
  200
}


val f: Future[Int] = for {
  f <- foo(1)
  b <- bar(1)
} yield {
  b
}

println(f)

//val result = Await.result(f, Duration.Inf)
//println(result)
