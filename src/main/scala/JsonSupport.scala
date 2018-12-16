import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models._
import spray.json._

trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val boardStateJsonFormatter = new JsonFormat[BoardState] {
    override def write(obj: BoardState): JsValue = {
      obj match {
        case Playing => JsString("Playing")
        case Won => JsString("Win")
        case Lose => JsString("Lose")
      }
    }

    override def read(json: JsValue): BoardState = ???
  }

  implicit val boardElementJsonFormatter = new JsonFormat[BoardElement] {
    override def write(obj: BoardElement): JsValue = {
      obj match {
        case Number(v) => JsNumber(v)
        case Explosion => JsString("Explosion")
        case Bomb => JsString("Bomb")
        case Unknown => JsString("Unknown")
        case Empty => JsString("Empty")
        case Flag => JsString("RedFlag")
      }
    }

    override def read(json: JsValue): BoardElement = ???
  }

  implicit val gameJsonFormat = jsonFormat5(Game.apply)
  implicit val prettyGameJsonFormat = jsonFormat5(GamePrettyResponse.apply)
}