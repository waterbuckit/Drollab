package Server

import java.awt.Color
import java.net.{ServerSocket, Socket}

import CanvasActions.CanvasAction

import scala.collection.mutable.ListBuffer


class Server(portArg: Int) {

  val serverSocket: ServerSocket = new ServerSocket(portArg)
  val users: ListBuffer[User] = ListBuffer()
  val pixels: Array[Array[Color]] = Array.ofDim[Color](50, 50)
  for (y <- pixels.indices) {
    for (x <- pixels.indices) {
      this.pixels(x)(y) = new Color((Math.random() * 255).asInstanceOf[Int],
        (Math.random() * 255).asInstanceOf[Int], (Math.random() * 255).asInstanceOf[Int])
    }
  }

  def arbitrateMessage(action: CanvasAction): Unit = {
    println(action)
    this.users.foreach(user => if (user.userID != action.userID)
      user.outputStream.writeObject(action))
  }

  def makeConnections(): Unit = {
    println("Listening on: " + this.serverSocket.getLocalPort)
    while (true) {
      val client: Socket = this.serverSocket.accept()
      println("Connected to: " + client.getInetAddress.getHostAddress)
      this.users.+=(new User(client, this.serverSocket, this))
      println("Current users: \n" + getUsers())
    }
  }


  def getUsers(): String = {
    val sb: StringBuilder = new StringBuilder()
    this.users.foreach(user => sb.append(user.userID + "\n"))
    return sb.toString()
  }

  def removeUser(user: User): Unit = {
    user.stopThread()
    this.users -= user
    println("Current users: \n" + getUsers())
  }
}
