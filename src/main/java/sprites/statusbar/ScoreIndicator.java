package sprites.statusbar;

import biuoop.DrawSurface;
import utils.Counter;

/**
 * Represents a score indicator at the status bar.
 */
public class ScoreIndicator extends Indicator {
    private Counter playerScore;

    /**
     * Create a score sprites.statusbar.Indicator.
     *
     * @param scoreCounter player's score
     */
    public ScoreIndicator(Counter scoreCounter) {
        this.playerScore = scoreCounter;
        this.setX(0); // just to init
        this.setWidth(200);
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
        String scoreStr
                = "Score: " + String.valueOf(this.playerScore.getValue());
        d.drawText(this.getX(), Indicator.START_Y, scoreStr,
                Indicator.TEXT_SIZE);
    }

    /**
     * Notify the sprite that time has passed.
     *
     * @param dt time change
     */
    @Override
    public void timePassed(double dt) {

    }
}
