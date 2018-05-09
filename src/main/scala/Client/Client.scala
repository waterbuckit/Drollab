package Client

import java.awt.Dimension
import java.io.ObjectInputStream

import javax.swing.{JFrame, JPanel}

class Client(inputStream: ObjectInputStream) {
  val WIDTH: Int = 510
  val HEIGHT: Int = 530

  val frame: JFrame = new JFrame()
  frame.setTitle("Client test")
  frame.setPreferredSize(new Dimension(WIDTH, HEIGHT))
  frame.setDefaultCloseOperation(3) // exit on close
  val drawPanel: DrawPanel = new DrawPanel()
  frame.add(drawPanel)
  frame.pack()
  frame.setSize(frame.getPreferredSize)
  val clientThread: Thread = new Thread(new ClientThread(inputStream, drawPanel))
  clientThread.start()
}

class ClientThread(inputStream: ObjectInputStream, panel: DrawPanel) extends Runnable {
  override def run(): Unit = {
    while (true) {
      inputStream.readObject().asInstanceOf[CanvasActions.CanvasAction].applyAction(panel.indexTest)
      panel.repaint()
    }
  }
}
