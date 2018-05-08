package Client

object ClientMain {
  def main(args: Array[String]): Unit = {
    val client : Client = new Client()
    client.frame.setVisible(true)
  }
}
