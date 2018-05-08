package Client

import java.awt.Dimension

import javax.swing.JFrame

class Client {
  val WIDTH : Int = 510
  val HEIGHT : Int = 530

  val frame : JFrame = new JFrame()
  frame.setTitle("Client test")
  frame.setPreferredSize(new Dimension(WIDTH, HEIGHT))
  frame.setDefaultCloseOperation(3) // exit on close
  val drawPanel : DrawPanel = new DrawPanel()
  frame.add(drawPanel)
  frame.pack()
  frame.setSize(frame.getPreferredSize)

}
