package Client

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.Socket

import CanvasActions.CanvasAction

object ClientMain {


  var socket: Socket = new Socket("localhost", 25566)
  var userCount: Int = 0;
  val outputStream: ObjectOutputStream = new ObjectOutputStream(socket.getOutputStream)
  val userID: String = "Adam2"


  def main(args: Array[String]): Unit = {
    System.setProperty("sun.java2d.opengl", "true")
    outputStream.writeObject(userID)
    val client: Client = new Client(new ObjectInputStream(socket.getInputStream))
    client.frame.setVisible(true)
  }

  def getUserCount: Int = this.userCount

  def sendAction(canvasAction: CanvasAction): Unit = {
    canvasAction.userID = userID
    outputStream.writeObject(canvasAction)
    outputStream.flush()
  }
}
