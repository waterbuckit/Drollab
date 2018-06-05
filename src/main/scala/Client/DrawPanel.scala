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
    // Temporary code for drawing the board
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
    // draw the mouse position
    g2d.setColor(new Color(selectedColor.getRed, selectedColor.getGreen, selectedColor.getBlue, 200))
    g2d.fillRect(mousePos.x * this.SCALE_FACTOR + offsetX, mousePos.y * this.SCALE_FACTOR + offsetY, this.SCALE_FACTOR,
      this.SCALE_FACTOR)
    // draw the user count and mouse location etc
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
      setMousePos(e)
      println(e.getPoint + " " + mousePos)
      repaint()
    }

    override def mousePressed(e: MouseEvent): Unit = {
      mousePressOffset.setLocation(e.getPoint)
    }

    override def mouseDragged(e: MouseEvent): Unit = {
      val offsetXTemp: Double = e.getPoint.getX - mousePressOffset.getX
      val offsetYTemp: Double = e.getPoint.getY - mousePressOffset.getY

      mousePressOffset.setLocation(e.getPoint)

      offsetX += offsetXTemp.toInt
      offsetY += offsetYTemp.toInt
      println("OFFSET: " + offsetX + ", " + offsetY)
      repaint()
    }

    override def mouseWheelMoved(e: MouseWheelEvent): Unit = {
      if((e.getWheelRotation * -1 > 0 && SCALE_FACTOR >= 70) || (e.getWheelRotation * -1 < 0 && SCALE_FACTOR <= 2)){
        return
      }
      //      val previousMidValue = ((getWidth / 2 - offsetX) / SCALE_FACTOR , (getHeight / 2 - offsetY) / SCALE_FACTOR)
      val previousMidValue = ((e.getX - offsetX) / SCALE_FACTOR, (e.getY - offsetY) / SCALE_FACTOR)
      println(previousMidValue + "DETAILS: " + (previousMidValue._1 * SCALE_FACTOR) + "," + (previousMidValue._2 * SCALE_FACTOR))
      val prevScaleFactor = SCALE_FACTOR
      SCALE_FACTOR += e.getWheelRotation * -1
      val newMid = (previousMidValue._1 * SCALE_FACTOR, previousMidValue._2 * SCALE_FACTOR)
      val diff = ((previousMidValue._1 * prevScaleFactor) - newMid._1, (previousMidValue._2 * prevScaleFactor) - newMid._2)
      offsetX += diff._1
      offsetY += diff._2
      // update the point at which to draw the mouse location.
      setMousePos(e)
      repaint()
    }

    override def mouseClicked(e: MouseEvent): Unit = {
      val index = (mousePos.x, mousePos.y)
      pixels(index._1)(index._2) = selectedColor
      repaint()
      ClientMain.sendAction(new CanvasActions.PixelChangedAction(index._1, index._2, selectedColor))
      println("Index = (" + index._1 + ")(" + index._2 + ") Clicked: " + e.getPoint)
    }

    def setMousePos(e: MouseEvent): Unit = {
      mousePos.setLocation(((e.getPoint.x - offsetX).asInstanceOf[Double] / SCALE_FACTOR).floor, ((e.getPoint.y - offsetY).asInstanceOf[Double] / SCALE_FACTOR).floor)
    }
  }

}
