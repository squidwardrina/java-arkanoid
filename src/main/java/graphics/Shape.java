package graphics;

import biuoop.DrawSurface;

/**
 * Represents a geometrical shape.
 */
public interface Shape {
    /**
     * Draws the shape on the surface.
     *
     * @param d the surface to draw on
     */
    void drawOn(DrawSurface d);
}
