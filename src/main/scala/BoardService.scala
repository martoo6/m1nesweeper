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
    //TODO: The recursive function is inneficient on a large matrix with lot's of bombs
    val arr: Array[Array[BoardElement]] = Array.fill(sizeY)(Array.fill(sizeX)(Empty))
    def placeMine(arr: Array[Array[BoardElement]]): Array[Array[BoardElement]] = {
      val x = Random.nextInt(sizeX)
      val y = Random.nextInt(sizeX)
      arr(x)(y) match {
        case Bomb => placeMine(arr)
        case Empty =>
          arr(x)(y) = Bomb
          arr
      }
    }
    (1 to mines).foldLeft(arr)((x, _) => placeMine(x))
  }

  def createBoard(sizeX: Int = 10, sizeY: Int = 10)(mines: Int = (sizeX*sizeY*0.1).toInt): Board = {
    val id = java.util.UUID.randomUUID().toString
    val board = Board(id, Array.fill(sizeY)(Array.fill(sizeX)(Unknown)), genBoard(sizeX, sizeY, mines), Playing, Instant.now().toEpochMilli)
    boards += board
    board
  }
}
