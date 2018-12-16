package models

case class Board(id: String,
                 userBoard: Array[Array[BoardElement]],
                 realBoard: Array[Array[BoardElement]],
                 state: BoardState,
                 timestamp: Long)
