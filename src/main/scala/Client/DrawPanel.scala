package Client

import java.awt.event.{MouseAdapter, MouseEvent, MouseWheelEvent}
import java.awt._

import javax.swing.JPanel

class DrawPanel(pixels: Array[Array[Color]]) extends JPanel {

  var newCalculatedMid = (0, 0)
  var SCALE_FACTOR: Int = 30
  val pixelsMem = pixels
  var offsetX = 0
  var offsetY = 0
  var mousePos: Point = new Point();
  val mouseEventHandler: MouseEventHandler = new MouseEventHandler
  val selectedColor: Color = Color.BLACK
  this.addMouseListener(mouseEventHandler)
  this.addMouseMotionListener(mouseEventHandler)
  this.addMouseWheelListener(mouseEventHandler)
  val rh: RenderingHints = new RenderingHints(
    RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
    g2d.setRenderingHints(this.rh)
    for (y <- pixels.indices) {
      for (x <- pixels.indices) {
        g2d.setColor(this.pixels(x)(y))
        g2d.fillRect((x) * this.SCALE_FACTOR + offsetX, (y) * this.SCALE_FACTOR + offsetY, this.SCALE_FACTOR,
          this.SCALE_FACTOR)
      }
    }
    drawUI(g2d)
  }

  def drawUI(g2d: Graphics2D): Unit = {
    g2d.setColor(new Color(selectedColor.getRed, selectedColor.getGreen, selectedColor.getBlue, 200))
    g2d.fillRect(mousePos.x * this.SCALE_FACTOR + offsetX, mousePos.y * this.SCALE_FACTOR + offsetY, this.SCALE_FACTOR,
      this.SCALE_FACTOR)
    g2d.setFont(new Font("Roboto Mono for Powerline", Font.PLAIN, 24))
    g2d.setStroke(new BasicStroke(50.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
    g2d.setColor(new Color(Color.BLACK.getRed, Color.BLACK.getGreen, Color.BLACK.getBlue, 200))
    g2d.drawLine(40, this.getHeight - 95, 40 + g2d.getFontMetrics.stringWidth(ClientMain.getUserCount.toString),
      this.getHeight - 95)
    g2d.drawLine(40, this.getHeight - 40, 40 + g2d.getFontMetrics.stringWidth("(" + mousePos.x + ", "
      + mousePos.y + ")"), this.getHeight - 40)
    g2d.setColor(Color.WHITE)
    g2d.drawString(ClientMain.getUserCount.toString, 40, this.getHeight - 87)
    g2d.drawString("(" + mousePos.x + ", " + mousePos.y + ")", 40, this.getHeight - 33)
  }

  class MouseEventHandler extends MouseAdapter {

    private val mousePressOffset: Point = new Point()

    override def mouseMoved(e: MouseEvent): Unit = {
      //      mousePos.setLocation((e.getPoint.x / SCALE_FACTOR) - offsetX, ((e.getPoint.y / SCALE_FACTOR) - offsetY))
      mousePos.setLocation((e.getPoint.x - offsetX) / SCALE_FACTOR, (e.getPoint.y - offsetY) / SCALE_FACTOR)
      repaint()
    }

    override def mousePressed(e: MouseEvent): Unit = {
      println("pressed at: " + e.getPoint)
      mousePressOffset.setLocation(e.getPoint)
    }

    override def mouseDragged(e: MouseEvent): Unit = {
      println("dragged")
      val offsetXTemp: Double = e.getPoint.getX - mousePressOffset.getX
      val offsetYTemp: Double = e.getPoint.getY - mousePressOffset.getY

      mousePressOffset.setLocation(e.getPoint)

      offsetX += offsetXTemp.toInt
      offsetY += offsetYTemp.toInt
      repaint()
    }

    override def mouseWheelMoved(e: MouseWheelEvent): Unit = {
      val previousMidValue = ((getWidth / 2 - offsetX) / SCALE_FACTOR , (getHeight / 2 - offsetX) / SCALE_FACTOR)
      val prevScaleFactor = SCALE_FACTOR
      //      val previousMid = ((getWidth / 2 - offsetX) / SCALE_FACTOR, (getHeight / 2 - offsetY) / SCALE_FACTOR)
      //      println("PREV: " + previousMid._1 + "," + previousMid._2)
      SCALE_FACTOR += e.getWheelRotation * -1
      val newMid = (previousMidValue._1 * SCALE_FACTOR, previousMidValue._2 * SCALE_FACTOR)
      val diff = ((previousMidValue._1 * prevScaleFactor) - newMid._1, (previousMidValue._2 * prevScaleFactor) - newMid._2)
      offsetX += diff._1
      offsetY += diff._2
      //      println("CURRENT MID: " + (getWidth / 2 - offsetX) + "," + (getHeight / 2 - offsetY))
      //      val midDiff = (previousMid._1 - ((getWidth / 2 - offsetX) / SCALE_FACTOR),
      //        previousMid._2 - ((getHeight / 2 - offsetY) / SCALE_FACTOR))
      //      println("DIFF: " + midDiff)
      //      println("CURRENT OFFSET: " + offsetX + "," + offsetY)
      //      //      newCalculatedMid = (midDiff._1 * SCALE_FACTOR + offsetX + (getWidth / 2 - offsetX), midDiff._2 * SCALE_FACTOR + offsetY + (getHeight / 2 - offsetY))
      //      newCalculatedMid = (midDiff._1 * SCALE_FACTOR , midDiff._2 * SCALE_FACTOR)
      //      println("NEW CALCULATED MID : " + newCalculatedMid + "INDEX: " + (newCalculatedMid._1 + (getWidth / 2 - offsetX) - offsetX) / SCALE_FACTOR +
      //        "," + (newCalculatedMid._2 + (getHeight / 2 - offsetY) - offsetY) / SCALE_FACTOR)
      //
      //            if (midDiff._1 != 0) offsetX -= newCalculatedMid._1
      //            if (midDiff._2 != 0) offsetY -= newCalculatedMid._2
      //      println("NEW OFFSET: " + offsetX + "," + offsetY)
      //      if (e.getWheelRotation < 0) {
      //        offsetX -=
      //        offsetY -=
      //      } else {
      //        offsetX +=
      //        offsetY +=
      //      }
      mousePos.setLocation((e.getPoint.x - offsetX) / SCALE_FACTOR, (e.getPoint.y - offsetY) / SCALE_FACTOR)
      repaint()
    }

    override def mouseClicked(e: MouseEvent): Unit = {
      val index = ((e.getPoint.x - offsetX) / SCALE_FACTOR, (e.getPoint.y - offsetY) / SCALE_FACTOR)
      //      val index = ((e.getPoint.x / SCALE_FACTOR) - offsetX, ((e.getPoint.y / SCALE_FACTOR) - offsetY))
      pixels(index._1)(index._2) = selectedColor
      repaint()
      ClientMain.sendAction(new CanvasActions.PixelChangedAction(index._1, index._2, selectedColor))
      println("Index = (" + index._1 + ")(" + index._2 + ") Clicked: " + e.getPoint)
    }
  }

}
