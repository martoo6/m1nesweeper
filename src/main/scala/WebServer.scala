import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.{HttpApp, Route}
import spray.json.DefaultJsonProtocol

object WebServer extends HttpApp with JsonSupport {
  import DefaultJsonProtocol._

  lazy val routes: Route = path("healthcheck"){
    complete {
      "OK"
    }
  } ~ pathPrefix("boards"){
    pathEnd {
      get {
        complete {
          BoardService.getBoards().toList
        }
      } ~
      post {
        complete {
          BoardService.createBoard()()
        }
      }
    } ~
      path(Segment) { id =>
        get {
          complete {
            BoardService.getBoard(id) match {
              case Some(x) => x
              case None => HttpResponse(NotFound)
            }
          }
        }
      }
  }
}
