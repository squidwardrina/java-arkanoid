package graphics;

import biuoop.DrawSurface;

/**
 * A geometrical circle.
 */
public class Circle implements Shape {
    private int xCenter;
    private int yCenter;
    private int radius;
    private java.awt.Color fillColor;
    private java.awt.Color drawColor;

    /**
     * Constructs a circle.
     *
     * @param xCenter   x coordinate of the center point
     * @param yCenter   y coordinate of the center point
     * @param radius    radius of the circle
     * @param fillColor fillColor of the circle
     * @param drawColor fillColor of the circle
     */
    public Circle(int xCenter, int yCenter, int radius,
                  java.awt.Color fillColor, java.awt.Color drawColor) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.radius = radius;
        this.fillColor = fillColor;
        this.drawColor = drawColor;
    }

    /**
     * Draws the shape on the surface.
     *
     * @param d the surface to draw on
     */
    public void drawOn(DrawSurface d) {
        d.setColor(this.fillColor);
        d.fillCircle(this.xCenter, this.yCenter, this.radius);
        d.setColor(this.drawColor);
        d.drawCircle(this.xCenter, this.yCenter, this.radius);
    }
}
