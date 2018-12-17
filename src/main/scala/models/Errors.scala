package models


object Errors {
  trait LogicError
  case object GameFinished extends Exception("Game finished") with LogicError
  case object InvalidPosition extends Exception("Invalid position") with LogicError
  case object ExistingFlag extends Exception("Can't click on flag") with LogicError
  case object ExistingNumber extends Exception("Can't click on number") with LogicError
  case object ExistingQuestionMark extends Exception("Can't click on question mark") with LogicError
  case object NotFoundError extends Exception
}
