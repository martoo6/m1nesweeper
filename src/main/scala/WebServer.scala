import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{BadRequest, NotFound}
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
          parameters('size.as[Int] ? 10, 'mines.as[Int].?) { (size, mines) =>
            complete {
              //Some simple validations
              val totalSize = if(size < 2 || size > 100) 10 else size
              val totalMines = mines.filter(x => x < size*size*0.8 && x > 0).getOrElse(Math.ceil(size*size*0.1).toInt)
              GameService.createGame(totalSize, totalMines).id
            }
          }
        }
    } ~
      pathPrefix(Segment) { id =>
        pathEnd {
          get {
            parameters('pretty.as[Boolean] ? false) { pretty =>
              complete {
                GameService.getGame(id) match {
                  case Some(game) => if (pretty) GamePrettyResponse.fromGame(game) else game
                  case None => HttpResponse(NotFound)
                }
              }
            }
          }
        } ~
          path(IntNumber / IntNumber / "click") { (x, y) =>
            post {
              parameters('pretty.as[Boolean] ? false) { pretty =>
                complete {
                  GameService.click(id, x, y) match {
                    case Some(Right(game)) => if(pretty) GamePrettyResponse.fromGame(game).userBoard else game.userBoard
                    case Some(Left(err)) => HttpResponse(BadRequest, entity = err)
                    case None => HttpResponse(NotFound)
                  }
                }
              }
            }
          } ~
          path(IntNumber / IntNumber / "flag") { (x, y) =>
            post {
              parameters('pretty.as[Boolean] ? false) { pretty =>
                complete {
                  GameService.flag(id, x, y) match {
                    case Some(Right(game)) => if(pretty) GamePrettyResponse.fromGame(game).userBoard else game.userBoard
                    case Some(Left(err)) => HttpResponse(BadRequest, entity = err)
                    case None => HttpResponse(NotFound)
                  }
                }
              }
            }
          }
      }
  }
}
