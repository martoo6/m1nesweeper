import java.time.Instant

import models.{Board, BoardElement, Bomb, Number, Playing, Unknown}

import scala.collection.mutable
import scala.util.Random

object BoardService {
  private val boards = mutable.ArrayBuffer[Board]()

  val options = Bomb :: Unknown :: (1 to 8).map(Number).toList

  def rndBoard =
    (1 to 5).map(x => (1 to 5).map(y => options(Random.nextInt(options.size))).toArray[BoardElement]).toArray

  boards += Board("1", rndBoard, rndBoard, Playing, Instant.now().toEpochMilli)

  def getBoards() = {
    boards.map(_.id)
  }

  def getBoard(id: String) = {
    boards.find(_.id == id)
  }
}
