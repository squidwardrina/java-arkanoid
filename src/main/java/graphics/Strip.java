package graphics;

import biuoop.DrawSurface;

/**
 * A geometrical line.
 */
public class Strip implements Shape {
    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;
    private java.awt.Color color;

    /**
     * Constructs a line.
     *
     * @param xStart x coordinate of the start point
     * @param yStart y coordinate of the start point
     * @param xEnd   x coordinate of the end point
     * @param yEnd   y coordinate of the end point
     * @param color  color of the line
     */
    public Strip(int xStart, int yStart, int xEnd, int yEnd,
                 java.awt.Color color) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.color = color;
    }

    /**
     * Draws the shape on the surface.
     *
     * @param d the surface to draw on
     */
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.drawLine(this.xStart, this.yStart, this.xEnd, this.yEnd);
    }
}
