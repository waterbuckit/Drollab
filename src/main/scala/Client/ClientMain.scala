package Client

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.Socket

import CanvasActions.CanvasAction

object ClientMain {

  var socket : Socket = new Socket("localhost", 25565)
  val outputStream : ObjectOutputStream = new ObjectOutputStream(socket.getOutputStream)
  val userID : String = "Test5"

  def main(args: Array[String]): Unit = {
    outputStream.writeObject(userID)
    val client : Client = new Client(new ObjectInputStream(socket.getInputStream))
    client.frame.setVisible(true)
  }

  def sendAction(canvasAction : CanvasAction) : Unit = {
    canvasAction.userID = userID
    outputStream.writeObject(canvasAction)
    outputStream.flush()
  }
}
