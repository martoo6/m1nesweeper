package models


case class GamePrettyResponse(id: String, board: Array[String], solution: Array[String], state: BoardState, timestamp: Long)

object GamePrettyResponse {
  def apply(game: Game): GamePrettyResponse =
    GamePrettyResponse(
      game.id, BoardPrettyPrinter.prettify(game.board),
      BoardPrettyPrinter.prettify(game.solution),
      game.state,
      game.timestamp)
}
