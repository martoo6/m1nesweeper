import akka.http.scaladsl.server.{HttpApp, Route}

object WebServer extends HttpApp {
  lazy val routes: Route = path("healthcheck"){
    complete {
      "OK"
    }
  }
}
