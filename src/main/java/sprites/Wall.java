package sprites;

import graphics.Point;

/**
 * Represents a wall block.
 */
public class Wall extends Block {
    /**
     * Create a new wall with location and width/height.
     *
     * @param upperLeft upper left vertex
     * @param width     width of the rectangle
     * @param height    height of the rectangle
     */
    public Wall(Point upperLeft, double width, double height) {
        super(upperLeft, width, height);
    }
}
