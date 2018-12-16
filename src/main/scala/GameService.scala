import models._

import scala.annotation.tailrec
import scala.collection.mutable

object GameService {
  private val games = mutable.HashMap[String, Game]()

  def getGames() = {
    games.keys
  }

  def getGame(id: String) = {
    games.get(id)
  }

  def createGame(size: Int = 10)(mines: Int = (size*size*0.1).toInt): Game = {
    val game = Game.build(size, mines)
    games += game.id -> game
    game
  }

  def click(id: String, posX: Int, posY: Int) = {
    games.get(id).map {
      case Game(_, _, _, state, _) if state != Playing => Left("Game finished")
      case Game(_, _, realBoard, _, _) if posX <= 0 && posX > realBoard.length && posY <= 0 && posY > realBoard.length => Left(s"Invalid position")
      case Game(_, userBoard, _, _, _) if userBoard(posY)(posX) == RedFlag => Left("Can't click on flag")
      case Game(_, userBoard, _, _, _) if userBoard(posY)(posX).isInstanceOf[Number] => Left("Can't click on number")

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
        Right(game)
    }
  }
}
