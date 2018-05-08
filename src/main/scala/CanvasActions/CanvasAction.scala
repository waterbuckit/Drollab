package CanvasActions

import java.awt.image.BufferedImage

trait CanvasAction[A] extends Serializable {
  override def toString: String = super.toString
  def applyAction(bufferedImage: BufferedImage) : Unit
}

case class MessageSentAction(messageString : String) extends CanvasAction[String] {

  override def toString: String = messageString

  override def applyAction(bufferedImage: BufferedImage): Unit = ???
}