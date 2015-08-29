package sprites;

import biuoop.DrawSurface;
import graphics.Point;
import graphics.Rectangle;

import java.awt.Color;
import java.awt.Image;

/**
 * Fills any rectangle with specified color or image.
 */
public class Fill {
    private Color fillColor = null;
    private Image fillImage = null;

    /**
     * Creates a color filling.
     *
     * @param fill color to fill
     */
    public Fill(Color fill) {
        this.fillColor = fill;
    }

    /**
     * Creates an image filling.
     *
     * @param fill image to fill
     */
    public Fill(Image fill) {
        this.fillImage = fill;
    }

    /**
     * Fills the rectangle.
     *
     * @param d draw surface
     * @param rect rectangle to fill
     */
    public void fillRectangle(DrawSurface d, Rectangle rect) {
        Point pos = rect.getUpperLeft();
        if (this.isColor()) {
            new Rectangle(pos, rect.getWidth(), rect.getHeight(), fillColor)
                    .drawOn(d);
        } else {
            d.drawImage((int) pos.getX(), (int) pos.getY(), fillImage);
        }
    }

    /**
     * Fills the whole screen.
     *
     * @param d draw surface
     */
    public void fillSurface(DrawSurface d) {
        if (this.isColor()) {
            new Rectangle(0, 0, d.getWidth(), d.getHeight(), fillColor)
                    .drawOn(d);
        } else {
            d.drawImage(0, 0, fillImage);
        }
    }

    /**
     * Checks if the fill is color or image.
     *
     * @return true if color, false if image
     */
    private boolean isColor() {
        return this.fillColor != null;
    }
}
