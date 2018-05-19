package CanvasActions

import java.awt.Color

trait CanvasAction extends Serializable {

  var userID : String

  override def toString: String = super.toString

  def applyAction(bufferedImage: Array[Array[Color]]): Unit
}

case class MessageSentAction(messageString: String) extends CanvasAction {
  override def toString: String = messageString

  override def applyAction(bufferedImage: Array[Array[Color]]): Unit = ???

  override var userID: String = _
}

case class UserCountChangeAction(count : Int) extends CanvasAction {
  override var userID: String = _

  override def applyAction(bufferedImage: Array[Array[Color]]): Unit = ???

  def userCount : Int = count
}

case class PixelChangedAction(xChanged: Int, yChanged: Int, colorChanged : Color) extends CanvasAction {
  override def toString: String = xChanged + ", " + yChanged

  override def applyAction(bufferedImage: Array[Array[Color]]): Unit = {
    bufferedImage(xChanged)(yChanged) = colorChanged
  }

  override var userID: String = _
}
