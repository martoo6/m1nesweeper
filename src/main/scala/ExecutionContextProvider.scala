import akka.dispatch.forkjoin.ForkJoinPool

import scala.concurrent.ExecutionContext

trait ExecutionContextProvider {
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(new ForkJoinPool())
}
