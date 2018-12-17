import models.BoardElement._
import models.BoardState._
import models.Errors.NotFoundError
import models._

import scala.concurrent.Future

object GameService extends ExecutionContextProvider  {
  def getGames() = {
    GameStorage.getAllGames
  }

  def getGame(id: String) = {
    GameStorage.getGame(id).flatMap {
      case Some(game) => Future.successful(game)
      case None => Future.failed(NotFoundError)
    }
  }

  def createGame(size: Int, mines: Int) = {
    val game = Game.build(size, mines)
    GameStorage.createGame(game).map(_ => game.id)
  }

  def isValidPosition(x: Int, y:Int, size: Int) = x >= 0 && x < size && y >= 0 && y < size

  def click(id: String, posX: Int, posY: Int) = {
    GameStorage.getGame(id).flatMap { _.map {
        case Game(_, _, _, state, _) if state != Playing => Future.failed(Errors.GameFinished)
        case Game(_, _, realBoard, _, _) if !isValidPosition(posX, posY, realBoard.length) => Future.failed(Errors.InvalidPosition)
        case Game(_, userBoard, _, _, _) if userBoard(posY)(posX) == Flag => Future.failed(Errors.ExistingFlag)
        case Game(_, userBoard, _, _, _) if userBoard(posY)(posX).isInstanceOf[Number] => Future.failed(Errors.ExistingNumber)
        case Game(_, userBoard, _, _, _) if userBoard(posY)(posX) == QuestionMark => Future.failed(Errors.ExistingQuestionMark)

        case game@Game(_, userBoard, realBoard, _, _) if realBoard(posY)(posX) == Bomb =>
          val loseGame = game.copy(state = Lose)
          userBoard(posY)(posX) = Explosion
          GameStorage.updateGame(loseGame).map(_ => loseGame)

        case game@Game(_, userBoard, realBoard, _, _) =>

          def setElement(x: Int, y: Int): Unit = {
            realBoard(y)(x) match {
              case Number(v) =>
                userBoard(y)(x) = Number(v)
                if (v == 0) {
                  Game.getValidPositions(x, y, realBoard.length)
                    .filter { case (pX, pY) => userBoard(pY)(pX) == Unknown }
                    .foreach { case (pX, pY) => setElement(pX, pY) }
                }
              case _ => //Do not process
            }
          }

          setElement(posX, posY)

          val returnGame = if (Game.win(userBoard, realBoard)) game.copy(state = Won) else game
          GameStorage.updateGame(returnGame).map(_ => returnGame)
      }.getOrElse(Future.failed(NotFoundError))
    }
  }

  def flag(id: String, posX: Int, posY: Int) = {
    GameStorage.getGame(id).flatMap {
      _.map {
        case game@Game(_, userBoard, realBoard, state, _) =>
          if (state != Playing) Future.failed(Errors.GameFinished)
          else if (!isValidPosition(posX, posY, realBoard.length)) Future.failed(Errors.InvalidPosition)
          else if (userBoard(posY)(posX).isInstanceOf[Number]) Future.failed(Errors.ExistingNumber)
          else if (userBoard(posY)(posX) == QuestionMark) Future.failed(Errors.ExistingQuestionMark)
          else if (userBoard(posY)(posX) == Flag) {
            game.userBoard(posY)(posX) = Unknown
            GameStorage.updateGame(game).map( _ => game)
          } else {
            game.userBoard(posY)(posX) = Flag
            val returnGame = if (Game.win(userBoard, realBoard)) game.copy(state = Won) else game
            GameStorage.updateGame(returnGame).map(_ => returnGame)
          }
      }.getOrElse(Future.failed(NotFoundError))
    }
  }

  def questionMark(id: String, posX: Int, posY: Int) = {
    GameStorage.getGame(id).flatMap {
      _.map {
        case game@Game(_, userBoard, realBoard, state, _) =>
          if (state != Playing) Future.failed(Errors.GameFinished)
          else if (!isValidPosition(posX, posY, realBoard.length)) Future.failed(Errors.InvalidPosition)
          else if (userBoard(posY)(posX).isInstanceOf[Number]) Future.failed(Errors.ExistingNumber)
          else if (userBoard(posY)(posX) == Flag) Future.failed(Errors.ExistingQuestionMark)
          else if (userBoard(posY)(posX) == QuestionMark) {
            game.userBoard(posY)(posX) = Unknown
            GameStorage.updateGame(game).map( _ => game)
          } else {
            game.userBoard(posY)(posX) = QuestionMark
            GameStorage.updateGame(game).map( _ => game)
          }
      }.getOrElse(Future.failed(NotFoundError))
    }
  }
}
