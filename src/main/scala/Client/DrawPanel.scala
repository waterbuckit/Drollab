package Client

import java.awt.event.{MouseAdapter, MouseEvent}
import java.awt.{Color, Graphics, Graphics2D, Point}

import javax.swing.JPanel

class DrawPanel extends JPanel {

  val SCALE_FACTOR: Int = 10
  var offsetX = 3
  var offsetY = 5
  val indexTest = Array.ofDim[Color](50, 50)
  var mousePos: Point = new Point();
  for (y <- indexTest.indices) {
    for (x <- indexTest.indices) {
      this.indexTest(x)(y) = new Color((Math.random() * 255).asInstanceOf[Int],
        (Math.random() * 255).asInstanceOf[Int], (Math.random() * 255).asInstanceOf[Int])
    }
  }
  val mouseEventHandler : MouseEventHandler = new MouseEventHandler
  this.addMouseListener(mouseEventHandler)
  this.addMouseMotionListener(mouseEventHandler)

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
    for (y <- indexTest.indices) {
      for (x <- indexTest.indices) {
        g2d.setColor(this.indexTest(x)(y))
        g2d.fillRect((x + offsetX) * this.SCALE_FACTOR, (y + offsetY) * this.SCALE_FACTOR, this.SCALE_FACTOR, this.SCALE_FACTOR)
      }
    }
  }

  class MouseEventHandler extends MouseAdapter {

//    private var offsetPoint : Point = new Point()
//
//    override def mouseDragged(e: MouseEvent): Unit = {
//      offsetX = e.getX - offsetPoint.x
//      offsetY = e.getY - offsetPoint.y
//
//      offsetPoint = e.getPoint
//      println("OFFSET: " + offsetX + "," + offsetY + " MOUSEPOS: " + e.getX + "," + e.getY)
//      repaint()
//    }

    override def mouseClicked(e: MouseEvent): Unit = {
      val index = ((e.getPoint.x / SCALE_FACTOR) - offsetX, ((e.getPoint.y / SCALE_FACTOR) - offsetY))
      indexTest(index._1)(index._2) = Color.GREEN
      repaint()
      ClientMain.sendAction(new CanvasActions.PixelChangedAction(index._1, index._2, Color.GREEN))
      println("Index = (" + index._1 + ")(" + index._2 + ") Clicked: " + e.getPoint.toString)
    }
  }
}
