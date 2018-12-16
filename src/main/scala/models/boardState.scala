package models

sealed trait BoardState
case object Playing extends BoardState
case object Won extends BoardState
case object Lose extends BoardState
