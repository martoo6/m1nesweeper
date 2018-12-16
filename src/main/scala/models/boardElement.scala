package models

sealed trait BoardElement
case class Number(value: Int) extends BoardElement
case object Explosion extends BoardElement
case object Bomb extends BoardElement
case object RedFlag extends BoardElement
case object Unknown extends BoardElement
case object Empty extends BoardElement