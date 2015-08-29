package sprites.statusbar;

import biuoop.DrawSurface;
import utils.Counter;

/**
 * Represents a lives indicator at the status bar.
 */
public class LivesIndicator extends Indicator {
    private Counter livesLeft;

    /**
     * Creates a lives Indicator.
     *
     * @param livesCount number of lives left
     */
    public LivesIndicator(Counter livesCount) {
        this.livesLeft = livesCount;
        setX(0); // just to init
        setWidth(200);
    }

    /**
     * Draw the sprite to the screen.
     *
     * @param d the draw surface
     */
    @Override
    public void drawOn(DrawSurface d) {
        // Display the score on the bar
        d.setColor(Indicator.TEXT_COLOR);
        String scoreStr = "Lives: " + String.valueOf(livesLeft.getValue());
        d.drawText(getX(), Indicator.START_Y, scoreStr, Indicator.TEXT_SIZE);

    }
}
