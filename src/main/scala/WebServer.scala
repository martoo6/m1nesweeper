import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.{HttpApp, Route}
import models.GamePrettyResponse
import spray.json.DefaultJsonProtocol

object WebServer extends HttpApp with JsonSupport {
  import DefaultJsonProtocol._

  lazy val routes: Route = path("healthcheck"){
    complete {
      "OK"
    }
  } ~ pathPrefix("games"){
    pathEnd {
      get {
        complete {
          GameService.getGames().toList
        }
      } ~
        post {
          complete {
            GameService.createGame()().id
          }
        }
    } ~
      path(Segment) { id =>
        get {
          parameters('pretty.as[Boolean] ? false) { pretty =>
            complete {
              GameService.getGame(id) match {
                case Some(game) => if(pretty) GamePrettyResponse.fromGame(game) else game
                case None => HttpResponse(NotFound)
              }
            }
          }
        }
      }
  }
}
