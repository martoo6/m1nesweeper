object Minesweeper extends App {
    val port: Int = sys.env.getOrElse("PORT", "8080").toInt
    WebServer.startServer("0.0.0.0", port)
}
