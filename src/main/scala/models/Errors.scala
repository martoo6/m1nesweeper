package models


object Errors {
  case object GameFinished extends Exception
  case object InvalidPosition extends Exception
  case object ExistingFlag extends Exception
  case object ExistingNumber extends Exception
  case object ExistingQuestionMark extends Exception
  case object NotFoundError extends Exception
}
