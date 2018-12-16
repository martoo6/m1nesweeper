package models

import java.time.Instant

import scala.util.Random

case class Game(id: String,
                userBoard: Array[Array[BoardElement]],
                realBoard: Array[Array[BoardElement]],
                state: BoardState = Playing,
                timestamp: Long = Instant.now().toEpochMilli)

object Game {
  type Board = Array[Array[BoardElement]]

  private def genBoard(sizeX: Int, sizeY: Int, mines: Int): Board = {
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

  def build(size: Int, mines: Int): Game = {
    val id =  sys.env.getOrElse("ID", java.util.UUID.randomUUID().toString)
    Game(id, Array.fill(size)(Array.fill(size)(Unknown)), genBoard(size, size, mines))
  }
}