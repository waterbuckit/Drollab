package CanvasActions

import java.awt.Color

trait CanvasAction extends Serializable {

  def userID: String = _

  override def toString: String = super.toString

  def applyAction(bufferedImage: Array[Array[Color]]): Unit
}

case class MessageSentAction(messageString: String) extends CanvasAction {
  override def toString: String = messageString

  override def applyAction(bufferedImage: Array[Array[Color]]): Unit = ???

}

case class PixelChangedAction(xChanged: Int, yChanged: Int) extends CanvasAction {
  override def toString: String = xChanged + ", " + yChanged

  override def applyAction(bufferedImage: Array[Array[Color]]): Unit = {

  }

}
