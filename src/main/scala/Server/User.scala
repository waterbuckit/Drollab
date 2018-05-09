package Server

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.{ServerSocket, Socket}

import CanvasActions.CanvasAction

class User(socket: Socket, serverSocket: ServerSocket, server: Server) {
  val inputStream = new ObjectInputStream(socket.getInputStream)
  val outputStream = new ObjectOutputStream(socket.getOutputStream)
  val userID = inputStream.readObject().asInstanceOf[String]
  println("New user: " + userID)
  val userThread: Thread = new Thread(new IncomingThread(inputStream, server))

  this.userThread.start()

  def stopThread(): Unit = {
    this.inputStream.close()
    this.outputStream.close()
    this.userThread.interrupt()
    this.socket.close()
    println(socket.getInetAddress.getHostAddress + " disconnected")
    server.users.-=(this)
  }

  def sendMessage(canvasAction: CanvasAction): Unit = {
    this.outputStream.writeObject(canvasAction)
    this.outputStream.flush()
  }
}

class IncomingThread(inputStream: ObjectInputStream, server: Server) extends Runnable {
  override def run(): Unit = {
    while(true){
      server.arbitrateMessage(inputStream.readObject().asInstanceOf[CanvasAction])
    }
  }
}