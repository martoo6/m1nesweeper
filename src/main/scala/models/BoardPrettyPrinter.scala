package models

import models.BoardElement._

object BoardPrettyPrinter {
  def prettify(board: Array[Array[BoardElement]]) =
    board.map(row => {
      row map {
        case Flag => "F"
        case Unknown => " "
        case Explosion => "#"
        case Bomb => "*"
        case QuestionMark => "?"
        case Number(v) => v.toString
      } mkString ""
    })
}
