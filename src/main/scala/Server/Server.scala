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

  def updateServerCanvas(x: Int, y: Int, colorChanged: Color): Unit = {
    this.pixels(x)(y) = colorChanged
  }

  def arbitrateMessage(action: CanvasAction): Unit = {
    println(action)

    action match {
      case CanvasActions.PixelChangedAction(x, y, colorChanged) => updateServerCanvas(x, y, colorChanged)
      case CanvasActions.UserCountChangeAction(count) => println("Current user count: " + count)
    }

    this.users.foreach(user => if (user.userID != action.userID)
      user.outputStream.writeObject(action))
  }

  def makeConnections(): Unit = {
    println("Listening on: " + this.serverSocket.getLocalPort)
    while (true) {
      val client: Socket = this.serverSocket.accept()
      println("Connected to: " + client.getInetAddress.getHostAddress)
      this.users.+=(new User(client, this.serverSocket, this))
      arbitrateMessage(new CanvasActions.UserCountChangeAction(this.users.length))
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
    arbitrateMessage(new CanvasActions.UserCountChangeAction(this.users.length))
    println("Current users: \n" + getUsers())
  }
}
