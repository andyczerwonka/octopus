package octopus.async.scalaz

import octopus.async.ToFuture
import scalaz.{-\/, \/-}
import scalaz.concurrent.Task

import scala.concurrent.{Future, Promise}

object ToFutureImplicits {

  implicit val scalazTaskToFuture: ToFuture[Task] = new ToFuture[Task] {

    def toFuture[A](value: Task[A]): Future[A] = {
      val p: Promise[A] = Promise()
      value.unsafePerformAsync {
        case -\/(ex) =>
          p.failure(ex)
          ()
        case \/-(r) =>
          p.success(r)
          ()
      }
      p.future
    }
  }
}
