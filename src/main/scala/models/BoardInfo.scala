package models

import models.Game.Board

object BoardInfo {
  case class BoardInfoResponse(id: String, board: Board, state: BoardState, timestamp: Long)
  object BoardInfoResponse{
    def apply(game: Game): BoardInfoResponse = BoardInfoResponse(game.id, game.board, game.state, game.timestamp)
  }

  case class PrettyBoardInfoResponse(id: String, board: Array[String], state: BoardState, timestamp: Long)
  object PrettyBoardInfoResponse{
    def apply(game: Game): PrettyBoardInfoResponse =
      PrettyBoardInfoResponse(game.id, BoardPrettyPrinter.prettify(game.board), game.state, game.timestamp)
  }
}
