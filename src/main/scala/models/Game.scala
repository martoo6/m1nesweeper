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

  val positions = for {
    x <- -1 to 1
    y <- -1 to 1
    if x != 0 || y !=0
  } yield x -> y

  def getValidPositions(x: Int, y:Int, size: Int) =
    positions
      .map { case (pX, pY) => (pX + x) -> (pY + y) }
      .filter { case (pX, pY) => pX >= 0 && pX < size && pY >= 0 && pY < size }

  private def generateBoard(size: Int, mines: Int): Board = {
    val board: Board = Array.ofDim(size, size)

    //TODO: Inneficient on a large matrix with lot's of bombs, not checking mines <= size*size (or make sense)
    def placeMine(): Unit = {
      val x = Random.nextInt(size)
      val y = Random.nextInt(size)
      board(x)(y) match {
        case Bomb => placeMine()
        case _ => board(x)(y) = Bomb
      }
    }

    def getBombCount(x: Int, y: Int, realBoard: Board) =
        getValidPositions(x, y, realBoard.length) count { case (pX, pY) => realBoard(pY)(pX) == Bomb }

    //TODO: non functional seems just wrong
    (1 to mines).foreach(_ => placeMine())

    for(y <- board.indices; x <- board.indices){
      if(board(y)(x) == null) board(y)(x) = Number(getBombCount(x, y, board))
    }

    board
  }

  def build(size: Int, mines: Int): Game = {
    val id =  sys.env.getOrElse("ID", java.util.UUID.randomUUID().toString)
    Game(id, Array.fill(size)(Array.fill(size)(Unknown)), generateBoard(size, mines))
  }
}