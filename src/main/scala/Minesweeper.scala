object Minesweeper extends App {
  override def main(args: Array[String]): Unit = {
    WebServer.startServer("localhost", 8080)
  }
}
