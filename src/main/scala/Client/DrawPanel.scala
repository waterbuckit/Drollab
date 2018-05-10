package Client

import java.awt.event.{MouseAdapter, MouseEvent}
import java.awt.{Color, Graphics, Graphics2D, Point}

import javax.swing.JPanel

class DrawPanel(pixels: Array[Array[Color]]) extends JPanel {

  val SCALE_FACTOR: Int = 30
  val pixelsMem = pixels
  var offsetX = 0
  var offsetY = 0
  var mousePos: Point = new Point();
  val mouseEventHandler: MouseEventHandler = new MouseEventHandler
  this.addMouseListener(mouseEventHandler)
  this.addMouseMotionListener(mouseEventHandler)

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
    for (y <- pixels.indices) {
      for (x <- pixels.indices) {
        g2d.setColor(this.pixels(x)(y))
        g2d.fillRect((x + offsetX) * this.SCALE_FACTOR, (y + offsetY) * this.SCALE_FACTOR, this.SCALE_FACTOR, this.SCALE_FACTOR)
      }
    }
  }

  class MouseEventHandler extends MouseAdapter {

    private var offsetPoint: Point = new Point()

    override def mousePressed(e: MouseEvent): Unit = {
      println("pressed")
      this.offsetPoint = e.getPoint
    }

    override def mouseDragged(e: MouseEvent): Unit = {
      println("dragged")
      offsetX = (e.getX - offsetPoint.x)/SCALE_FACTOR
      offsetY = (e.getY - offsetPoint.y)/SCALE_FACTOR
      println("OFFSET: " + offsetX + "," + offsetY + " MOUSEPOS: " + e.getX + "," + e.getY)
      repaint()
    }
    override def mouseClicked(e: MouseEvent): Unit = {
      val index = ((e.getPoint.x / SCALE_FACTOR) - offsetX, ((e.getPoint.y / SCALE_FACTOR) - offsetY))
      pixels(index._1)(index._2) = Color.GREEN
      repaint()
      ClientMain.sendAction(new CanvasActions.PixelChangedAction(index._1, index._2, Color.GREEN))
      println("Index = (" + index._1 + ")(" + index._2 + ") Clicked: " + e.getPoint.toString)
    }
  }

}
