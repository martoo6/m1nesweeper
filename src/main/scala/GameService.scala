import models._

import scala.collection.mutable

object GameService {
  private val games = mutable.HashMap[String, Game]()

  def getGames() = {
    games.keys
  }

  def getGame(id: String) = {
    games.get(id)
  }

  def createGame(size: Int, mines: Int): Game = {
    val game = Game.build(size, mines)
    games += game.id -> game
    game
  }

  def isValidPosition(x: Int, y:Int, size: Int) = x >= 0 && x < size && y >= 0 && y < size

  def click(id: String, posX: Int, posY: Int) = {
    games.get(id).map {
      case Game(_, _, _, state, _) if state != Playing => Left("Game finished")
      case Game(_, _, realBoard, _, _) if !isValidPosition(posX, posY, realBoard.length) => Left(s"Invalid position")
      case Game(_, userBoard, _, _, _) if userBoard(posY)(posX) == Flag => Left("Can't click on flag")
      case Game(_, userBoard, _, _, _) if userBoard(posY)(posX).isInstanceOf[Number] => Left("Can't click on number")
      case Game(_, userBoard, _, _, _) if userBoard(posY)(posX) == QuestionMark => Left("Can't click on question mark")

      case game @ Game(_, userBoard, realBoard, _, _) if realBoard(posY)(posX) == Bomb =>
        val loseGame = game.copy(state = Lose)
        userBoard(posY)(posX) = Explosion
        games(id) = loseGame
        Right(loseGame)

      case game @ Game(_, userBoard, realBoard, _, _) =>

        def setElement(x: Int, y: Int): Unit = {
          realBoard(y)(x) match {
            case Number(v) =>
              userBoard(y)(x) = Number(v)
              if(v == 0) {
                Game.getValidPositions(x, y, realBoard.length)
                  .filter { case (pX, pY) => userBoard(pY)(pX) == Unknown }
                  .foreach { case (pX, pY) => setElement(pX, pY) }
              }
            case _ => //Do not process
          }
        }

        setElement(posX, posY)

        if(Game.win(userBoard, realBoard)) {
          val wonGame = game.copy(state = Won)
          games(id) = wonGame
          Right(wonGame)
        } else {
          Right(game)
        }
    }
  }

  def flag(id: String, posX: Int, posY: Int) = {
    games.get(id).map { case game @ Game(_, userBoard, realBoard, state, _) =>
      if (state != Playing) Left("Game finished")
      else if (!isValidPosition(posX, posY, realBoard.length)) Left("Invalid position")
      else if (userBoard(posY)(posX).isInstanceOf[Number]) Left("Can't click on number")
      else if (userBoard(posY)(posX) == QuestionMark) Left("Can't click on question mark")
      else if (userBoard(posY)(posX) == Flag) {
        game.userBoard(posY)(posX) = Unknown
        Right(game)
      } else {
        game.userBoard(posY)(posX) = Flag
        if (Game.win(userBoard, realBoard)) {
          val wonGame = game.copy(state = Won)
          games(id) = wonGame
          Right(wonGame)
        } else {
          Right(game)
        }
      }
    }
  }

  def questionMark(id: String, posX: Int, posY: Int) = {
    games.get(id).map { case game @ Game(_, userBoard, realBoard, state, _) =>
      if (state != Playing) Left("Game finished")
      else if (!isValidPosition(posX, posY, realBoard.length)) Left("Invalid position")
      else if (userBoard(posY)(posX).isInstanceOf[Number]) Left("Can't click on number")
      else if (userBoard(posY)(posX) == Flag) Left("Can't click on flag")
      else if (userBoard(posY)(posX) == QuestionMark) {
        game.userBoard(posY)(posX) = Unknown
        Right(game)
      } else {
        game.userBoard(posY)(posX) = QuestionMark
        Right(game)
      }
    }
  }
}
