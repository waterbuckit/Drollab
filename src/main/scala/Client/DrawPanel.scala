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
        g2d.fillRect((x + offsetX) * this.SCALE_FACTOR, (y + offsetY) * this.SCALE_FACTOR, this.SCALE_FACTOR, this.SCALE_FACTOR)
      }
    }
    drawUI(g2d)
  }


  def drawUI(g2d: Graphics2D): Unit = {
    g2d.setStroke(new BasicStroke(50.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
    g2d.setColor(new Color(Color.BLACK.getRed, Color.BLACK.getGreen, Color.BLACK.getBlue, 200))
    // Users
    g2d.drawLine(40, this.getHeight - 95, 60, this.getHeight - 95)
    // Mouse position in canvas.
    g2d.drawLine(40, this.getHeight - 40, 60, this.getHeight - 40)
    g2d.setColor(Color.WHITE)
    g2d.drawString(ClientMain.getUserCount.toString, 50, this.getHeight - 95)
    g2d.drawString("(" + mousePos.x + ", " + mousePos.y + ")", 50, this.getHeight - 40)
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

      println(offsetXTemp + "," + offsetYTemp)
      println("PRINT 1 OFFSET: " + offsetX + "," + offsetY + " MOUSEPOS: " + e.getX + "," + e.getY + "OFFSETPOINT: " + mousePressOffset.x + ", " + mousePressOffset.y)
      mousePressOffset.setLocation(e.getPoint)

      offsetX = offsetXTemp.toInt
      offsetY = offsetYTemp.toInt
      println("PRINT 2 OFFSET: " + offsetX + "," + offsetY + " MOUSEPOS: " + e.getX + "," + e.getY + "OFFSETPOINT: " + mousePressOffset.x + ", " + mousePressOffset.y)
      repaint()
    }

    override def mouseWheelMoved(e: MouseWheelEvent): Unit = {
      SCALE_FACTOR += e.getWheelRotation
      repaint()
    }

    override def mouseClicked(e: MouseEvent): Unit = {
      val index = ((e.getPoint.x / SCALE_FACTOR) - offsetX, ((e.getPoint.y / SCALE_FACTOR) - offsetY))
      pixels(index._1)(index._2) = Color.GREEN
      repaint()
      ClientMain.sendAction(new CanvasActions.PixelChangedAction(index._1, index._2, Color.GREEN))
      println("Index = (" + index._1 + ")(" + index._2 + ") Clicked: " + e.getPoint)
    }
  }

}
