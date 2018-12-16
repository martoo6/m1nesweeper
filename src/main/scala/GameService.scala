import models._

import scala.collection.mutable

object GameService {
  private val games = mutable.ArrayBuffer[Game]()

  def getGames() = {
    games.map(_.id)
  }

  def getGame(id: String) = {
    games.find(_.id == id)
  }

  def createGame(size: Int = 10)(mines: Int = (size*size*0.1).toInt): Game = {
    val game = Game.build(size, mines)
    games += game
    game
  }
}
