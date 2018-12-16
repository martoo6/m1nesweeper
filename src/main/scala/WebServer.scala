import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{HttpApp, Route}

object WebServer extends HttpApp with SprayJsonSupport {
  import spray.json.DefaultJsonProtocol._

  lazy val routes: Route = path("healthcheck"){
    complete {
      "OK"
    }
  } ~ path("boards"){
    get {
      complete {
        Array(1, 2, 3, 4)
      }
    }
  }
}
