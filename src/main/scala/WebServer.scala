import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{BadRequest, InternalServerError, NotFound}
import akka.http.scaladsl.server.{ExceptionHandler, HttpApp, Route}
import models.Errors._
import models.GamePrettyResponse

object WebServer extends HttpApp with JsonSupport with ExecutionContextProvider {

  val exceptionHandler = ExceptionHandler {
    //TODO: each exception should contain it's own message
    case GameFinished => complete(HttpResponse(BadRequest, entity = "Game finished"))
    case InvalidPosition => complete(HttpResponse(BadRequest, entity = "Invalid position"))
    case ExistingFlag => complete(HttpResponse(BadRequest, entity = "Can't click on flag"))
    case ExistingNumber => complete(HttpResponse(BadRequest, entity = "Can't click on number"))
    case ExistingQuestionMark => complete(HttpResponse(BadRequest, entity = "Can't click on question mark"))
    case NotFoundError => complete(HttpResponse(NotFound))
    case e => println(e) //TODO: Add proper logging
      complete(HttpResponse(InternalServerError))
  }

  lazy val routes: Route =
    handleExceptions(exceptionHandler) {
      path("healthcheck") {
        complete {
          "OK"
        }
      } ~ pathPrefix("games") {
        pathEnd {
          get {
            complete {
              GameService.getGames().map(_.map(_.id))
            }
          } ~
            post {
              parameters('size.as[Int] ? 10, 'mines.as[Int].?) { (size, mines) =>
                complete {
                  //Some simple validations
                  val totalSize = if (size < 2 || size > 100) 10 else size
                  val totalMines = mines.filter(x => x < size * size * 0.8 && x > 0).getOrElse(Math.ceil(size * size * 0.1).toInt)
                  GameService.createGame(totalSize, totalMines)
                }
              }
            }
        } ~
          pathPrefix(Segment) { id =>
            pathEnd {
              get {
                parameters('pretty.as[Boolean] ? false) { pretty =>
                  onSuccess(GameService.getGame(id)) { game =>
                    if (pretty) complete(GamePrettyResponse.fromGame(game)) else complete(game)
                  }
                }
              }
            } ~
              path(IntNumber / IntNumber / "click") { (x, y) =>
                post {
                  parameters('pretty.as[Boolean] ? false) { pretty =>
                    onSuccess(GameService.click(id, x, y)) { game =>
                        if (pretty) complete(GamePrettyResponse.fromGame(game).userBoard) else complete(game.userBoard)
                    }
                  }
                }
              } ~
              path(IntNumber / IntNumber / "flag") { (x, y) =>
                post {
                  parameters('pretty.as[Boolean] ? false) { pretty =>
                    onSuccess(GameService.flag(id, x, y)) { game =>
                      if (pretty) complete(GamePrettyResponse.fromGame(game).userBoard) else complete(game.userBoard)
                    }
                  }
                }
              } ~
              path(IntNumber / IntNumber / "question-mark") { (x, y) =>
                post {
                  parameters('pretty.as[Boolean] ? false) { pretty =>
                    onSuccess(GameService.questionMark(id, x, y)) {
                      game => if (pretty) complete(GamePrettyResponse.fromGame(game).userBoard) else complete(game.userBoard)
                    }
                  }
                }
              }
          }
      }
    }
}
