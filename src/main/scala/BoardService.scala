import java.time.Instant

import models._

import scala.collection.mutable
import scala.util.Random

object BoardService {
  private val boards = mutable.ArrayBuffer[Board]()

  def getBoards() = {
    boards.map(_.id)
  }

  def getBoard(id: String) = {
    boards.find(_.id == id)
  }


  private def genBoard(sizeX: Int, sizeY: Int, mines: Int): Array[Array[BoardElement]] = {
    var totalMines = mines
    (1 to sizeY).map(_ =>
      (1 to sizeX).map(_ =>  {
        if(totalMines > 0 && Random.nextBoolean()) {
          totalMines-=1
          Bomb
        } else {
          Empty
        }
      }
      ).toArray[BoardElement]
    ).toArray
  }

  def createBoard(sizeX: Int = 10, sizeY: Int = 10)(mines: Int = (sizeX*sizeY*0.1).toInt): Board = {
    val id = java.util.UUID.randomUUID().toString
    val board = Board(id, Array.fill(sizeY)(Array.fill(sizeX)(Unknown)), genBoard(sizeX, sizeY, mines), Playing, Instant.now().toEpochMilli)
    boards += board
    board
  }
}
