package Server

import java.net.{ServerSocket, Socket}

import CanvasActions.CanvasAction

import scala.collection.mutable.ListBuffer


class Server(portArg: Int) {

  val serverSocket: ServerSocket = new ServerSocket(portArg)
  val users: ListBuffer[User] = ListBuffer()

  def arbitrateMessage(action: CanvasAction): Unit = {
    println(action)
    this.users.foreach(user => if (user.userID != action.userID)
      user.outputStream.writeObject("Action received"))
  }

  def makeConnections(): Unit = {
    println("Listening on: " + this.serverSocket.getLocalPort)
    while (true) {
      val client: Socket = this.serverSocket.accept()
      println("Connected to: " + client.getInetAddress.getHostAddress)
      this.users.+=(new User(client, this.serverSocket, this))
    }
  }
}
