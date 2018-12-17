import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.BoardElement._
import models.BoardInfo.{BoardInfoResponse, PrettyBoardInfoResponse}
import models.BoardState._
import models._
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  import DefaultJsonProtocol._

  implicit val boardStateJsonFormatter = new JsonFormat[BoardState] {
    override def write(obj: BoardState): JsValue = {
      obj match {
        case Playing => JsString("Playing")
        case Won => JsString("Win")
        case Lose => JsString("Lose")
        case _ => ???
      }
    }

    override def read(json: JsValue): BoardState = json match {
      case JsString("Playing") => Playing
      case JsString("Win") => Won
      case JsString("Lose") => Lose
      case _ => ???
    }
  }

  implicit val boardElementJsonFormatter = new JsonFormat[BoardElement] {
    override def write(obj: BoardElement): JsValue = {
      obj match {
        case Number(v) => JsNumber(v)
        case Explosion => JsString("Explosion")
        case Bomb => JsString("Bomb")
        case Unknown => JsString("Unknown")
        case QuestionMark => JsString("QuestionMark")
        case Flag => JsString("Flag")
        case _ => ???
      }
    }

    override def read(json: JsValue): BoardElement = json match {
      case JsNumber(v) => Number(v.toInt)
      case JsString("Explosion") => Explosion
      case JsString("Bomb") => Bomb
      case JsString("Unknown") => Unknown
      case JsString("QuestionMark") => QuestionMark
      case JsString("Flag") => Flag
      case _ => ???
    }
  }

  implicit val gameJsonFormat = jsonFormat5(Game.apply)
  implicit val prettyGameJsonFormat = jsonFormat5(GamePrettyResponse.apply)

  implicit val boardInfoResponsesonFormat = jsonFormat4(BoardInfoResponse.apply)
  implicit val prettyBoardInfoResponseJsonFormat = jsonFormat4(PrettyBoardInfoResponse.apply)
}