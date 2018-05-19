package Client

import java.awt.event.{MouseAdapter, MouseEvent, MouseWheelEvent}
import java.awt._

import javax.swing.JPanel

class DrawPanel(pixels: Array[Array[Color]]) extends JPanel {

  var SCALE_FACTOR: Int = 30
  val pixelsMem = pixels
  var offsetX = 0
  var offsetY = 0
  var mousePos: Point = new Point();
  val mouseEventHandler: MouseEventHandler = new MouseEventHandler
  val selectedColor : Color = Color.BLACK
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
        g2d.fillRect((x + offsetX) * this.SCALE_FACTOR, (y + offsetY) * this.SCALE_FACTOR, this.SCALE_FACTOR,
          this.SCALE_FACTOR)
      }
    }
    drawUI(g2d)
  }

  def drawUI(g2d: Graphics2D): Unit = {
    g2d.setColor(new Color(selectedColor.getRed, selectedColor.getGreen, selectedColor.getBlue,200))
    g2d.fillRect((mousePos.x + offsetX) * this.SCALE_FACTOR, (mousePos.y + offsetY) * this.SCALE_FACTOR, this.SCALE_FACTOR,
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
      mousePos.setLocation((e.getPoint.x / SCALE_FACTOR) - offsetX, ((e.getPoint.y / SCALE_FACTOR) - offsetY))
      repaint()
    }

    override def mousePressed(e: MouseEvent): Unit = {
      println("pressed at: " + e.getPoint)
      mousePressOffset.setLocation(e.getPoint)
    }

    override def mouseDragged(e: MouseEvent): Unit = {
      println("dragged")
      val offsetXTemp: Double = (e.getPoint.getX - mousePressOffset.getX) / SCALE_FACTOR
      val offsetYTemp: Double = (e.getPoint.getY - mousePressOffset.getY) / SCALE_FACTOR

      offsetX = offsetXTemp.toInt
      offsetY = offsetYTemp.toInt
      println("PRINT 2 OFFSET: " + offsetX + "," + offsetY + " MOUSEPOS: " + e.getX + "," + e.getY + "OFFSETPOINT: " + mousePressOffset.x + ", " + mousePressOffset.y)
      repaint()
    }

    override def mouseWheelMoved(e: MouseWheelEvent): Unit = {
      SCALE_FACTOR += e.getWheelRotation
      mousePos.setLocation((e.getPoint.x / SCALE_FACTOR) - offsetX, ((e.getPoint.y / SCALE_FACTOR) - offsetY))
      repaint()
    }

    override def mouseClicked(e: MouseEvent): Unit = {
      val index = ((e.getPoint.x / SCALE_FACTOR) - offsetX, ((e.getPoint.y / SCALE_FACTOR) - offsetY))
      pixels(index._1)(index._2) = selectedColor
      repaint()
      ClientMain.sendAction(new CanvasActions.PixelChangedAction(index._1, index._2, selectedColor))
      println("Index = (" + index._1 + ")(" + index._2 + ") Clicked: " + e.getPoint)
    }
  }

}
