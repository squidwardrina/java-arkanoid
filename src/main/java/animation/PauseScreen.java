package animation;

import biuoop.DrawSurface;
import graphics.Rectangle;
import utils.Finals;

import java.awt.Color;

/**
 * Represents the pause screen.
 */
public class PauseScreen implements Animation {
    /**
     * Prepare one animation step.
     *
     * @param d  draw surface
     * @param dt time passed since last move
     */
    public void doOneFrame(DrawSurface d, double dt) {
        Color backgColor = new Color(205, 252, 228);
        Color textColor = new Color(51, 175, 150);

        // Draw background
        new Rectangle(0, 0, d.getWidth(), d.getHeight(), backgColor).drawOn(d);

        // Draw text
        d.setColor(textColor);
        String stopKey = Finals.getInstance().getStopAnimationKey();
        d.drawText(180, 80, "Paused. Press " + stopKey + " to continue", 30);
    }

    /**
     * Checks if the animation should be stopped.
     *
     * @return check result
     */
    public boolean shouldStop() {
        return false;
    }
}