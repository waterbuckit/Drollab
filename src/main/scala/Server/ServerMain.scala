package Server

object ServerMain {

  def main(args: Array[String]): Unit = {
//    val server : Server = new Server(args(0).toInt)
    val server : Server = new Server(25565)
    server.makeConnections()
  }

}
