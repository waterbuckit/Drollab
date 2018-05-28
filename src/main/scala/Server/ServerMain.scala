package Server

import javax.swing.{JButton, JFrame, JLabel, JTextField}


object ServerMain {

  def main(args: Array[String]): Unit = {
//    val server : Server = new Server(args(0).toInt)
//    val portSelectFrame : JFrame = new JFrame()
//    portSelectFrame.setResizable(false)
//    portSelectFrame.add(new JLabel("Port: "))
//    val textField : JTextField = new JTextField()
//    val button : JButton = new JButton()
//    portSelectFrame.add(textField)
    val server : Server = new Server(25566)
    server.makeConnections()
  }

}
