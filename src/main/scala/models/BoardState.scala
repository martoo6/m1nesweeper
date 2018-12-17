package models

sealed trait BoardState
object BoardState {
  case object Won extends BoardState
  case object Lose extends BoardState
  case object Playing extends BoardState
}
