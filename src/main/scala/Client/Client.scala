package Client

import java.awt.{Color, Dimension}
import java.io.{IOException, ObjectInputStream}

import javax.swing.{JFrame, JPanel}

class Client(inputStream: ObjectInputStream) {
  val WIDTH: Int = 510
  val HEIGHT: Int = 530
  // get the current state of the array
  val currentPixels : Array[Array[Color]] = inputStream.readObject().asInstanceOf[Array[Array[Color]]]

  val frame: JFrame = new JFrame()
  frame.setTitle("Client test")
  frame.setPreferredSize(new Dimension(WIDTH, HEIGHT))
  frame.setDefaultCloseOperation(3) // exit on close
  val drawPanel: DrawPanel = new DrawPanel(currentPixels)
  frame.add(drawPanel)
  frame.pack()
  frame.setSize(frame.getPreferredSize)
  val clientThread: Thread = new Thread(new ClientThread(inputStream, drawPanel))
  clientThread.start()
}

class ClientThread(inputStream: ObjectInputStream, panel: DrawPanel) extends Runnable {
  override def run(): Unit = {
    while (true) {
      try {
        inputStream.readObject().asInstanceOf[CanvasActions.CanvasAction].applyAction(panel.pixelsMem)
        panel.repaint()
      } catch {
        case ioe : IOException => {
          println("Server error")
          System.exit(0)
        }
      }
    }
  }
}
