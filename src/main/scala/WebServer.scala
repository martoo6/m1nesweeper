import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{BadRequest, InternalServerError, NotFound}
import akka.http.scaladsl.server.{ExceptionHandler, HttpApp, Route}
import models.BoardInfo.{BoardInfoResponse, PrettyBoardInfoResponse}
import models.Errors._
import models.GamePrettyResponse

object WebServer extends HttpApp with JsonSupport with ExecutionContextProvider {

  val exceptionHandler = ExceptionHandler {
    case e: LogicError => complete(HttpResponse(BadRequest, entity = e.getMessage))
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
              parameters('size.as[Int] ? 10, 'mines.as[Int].?, 'pretty.as[Boolean] ? false) { (size, mines, pretty) =>
                //Some simple validations
                val totalSize = if (size < 2 || size > 100) 10 else size
                val totalMines = mines.filter(x => x < size * size * 0.8 && x > 0).getOrElse(Math.ceil(size * size * 0.1).toInt)
                onSuccess(GameService.createGame(totalSize, totalMines)) { game =>
                  if (pretty) complete(PrettyBoardInfoResponse(game)) else complete(BoardInfoResponse(game))
                }
              }
            }
        } ~
          pathPrefix(Segment) { id =>
            pathEnd {
              get {
                parameters('pretty.as[Boolean] ? false) { pretty =>
                  onSuccess(GameService.getGame(id)) { game =>
                    if (pretty) complete(GamePrettyResponse(game)) else complete(game)
                  }
                }
              }
            } ~
              path(IntNumber / IntNumber / "click") { (x, y) =>
                post {
                  parameters('pretty.as[Boolean] ? false) { pretty =>
                    onSuccess(GameService.click(id, x, y)) { game =>
                      if (pretty) complete(PrettyBoardInfoResponse(game)) else complete(BoardInfoResponse(game))
                    }
                  }
                }
              } ~
              path(IntNumber / IntNumber / "flag") { (x, y) =>
                post {
                  parameters('pretty.as[Boolean] ? false) { pretty =>
                    onSuccess(GameService.flag(id, x, y)) { game =>
                      if (pretty) complete(PrettyBoardInfoResponse(game)) else complete(BoardInfoResponse(game))
                    }
                  }
                }
              } ~
              path(IntNumber / IntNumber / "question-mark") { (x, y) =>
                post {
                  parameters('pretty.as[Boolean] ? false) { pretty =>
                    onSuccess(GameService.questionMark(id, x, y)) { game =>
                      if (pretty) complete(PrettyBoardInfoResponse(game)) else complete(BoardInfoResponse(game))
                    }
                  }
                }
              }
          }
      }
    }
}
