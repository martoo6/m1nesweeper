package models

case class GamePrettyResponse(id: String, userBoard: Array[String], realBoard: Array[String], state: BoardState, timestamp: Long)

object GamePrettyResponse {
  def boardPrettyPrint(board: Array[Array[BoardElement]]) =
    board.map(row => {
      row map {
        case RedFlag => "F"
        case Unknown => " "
        case Explosion => "#"
        case Bomb => "*"
        case Number(v) => v.toString
      } mkString ""
    })

  def fromGame(game: Game): GamePrettyResponse =
    GamePrettyResponse(game.id, boardPrettyPrint(game.userBoard), boardPrettyPrint(game.realBoard), game.state, game.timestamp)
}
