package Client

import java.awt.event.{MouseAdapter, MouseEvent}
import java.awt.{Color, Graphics, Graphics2D, Point}

import javax.swing.JPanel

class DrawPanel extends JPanel {

  val SCALE_FACTOR: Int = 10
  val indexTest = Array.ofDim[Color](50, 50)
  var mousePos : Point = new Point();
  for (y <- 0 to indexTest.length - 1) {
    for (x <- 0 to indexTest.length - 1) {
      this.indexTest(x)(y) = new Color((Math.random() * 255).asInstanceOf[Int],
        (Math.random() * 255).asInstanceOf[Int], (Math.random() * 255).asInstanceOf[Int])
    }
  }
  this.addMouseListener(new MouseAdapter() {
    override def mouseClicked(e: MouseEvent): Unit = {
      println("Index = (" + (e.getPoint.x/SCALE_FACTOR) + ")("+ (e.getPoint().y/SCALE_FACTOR) + ") Clicked: "+ e.getPoint.toString)
    }
  })

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
    for (y <- 0 to indexTest.length - 1) {
      for (x <- 0 to indexTest.length - 1) {
        g2d.setColor(this.indexTest(x)(y))
        g2d.fillRect(x * this.SCALE_FACTOR, y * this.SCALE_FACTOR, this.SCALE_FACTOR, this.SCALE_FACTOR)
      }
    }
  }
}
