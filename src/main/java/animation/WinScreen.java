package animation;

import biuoop.DrawSurface;
import graphics.Circle;
import graphics.Rectangle;
import graphics.Strip;

import java.awt.Color;

/**
 * Represents the "you win" screen.
 */
public class WinScreen implements Animation {
    private int score;

    /**
     * Create the end screen.
     *
     * @param finalScore is the final score
     */
    public WinScreen(int finalScore) {
        this.score = finalScore;
    }

    /**
     * Prepare one animation step.
     *
     * @param d draw surface
     * @param dt is the time of the move
     */
    public void doOneFrame(DrawSurface d, double dt) {
        // Draw the screen
        drawBackground(d);
        drawMessage(d);
    }

    /**
     * Checks if the animation should be stopped.
     *
     * @return check result
     */
    public boolean shouldStop() {
        return false;
    }

    /**
     * Draws the background of the screen.
     *
     * @param d draw surface
     */
    private void drawBackground(DrawSurface d) {
        // Draw frame for user message
        new Rectangle(0, 0, d.getWidth(), d.getHeight(), Color.BLACK).drawOn(d);
        new Rectangle(0, d.getHeight() / 2 - 30, d.getWidth(), 32, Color.WHITE)
                .drawOn(d);

        // Add lines
        int xCenter = 775;
        int yCenter = 30;
        int xLineEnd = 0;
        int yLineEnd = d.getHeight() / 2 - 30;
        int dx = d.getWidth() / 50;
        Color color = Color.ORANGE;
        for (int i = 0; i < 50; i++) {
            xLineEnd += dx;
            new Strip(xCenter, yCenter, xLineEnd, yLineEnd, color).drawOn(d);
        }
        new Circle(xCenter, yCenter, 80, Color.YELLOW, Color.ORANGE).drawOn(d);
    }

    /**
     * Draws the text message to user.
     *
     * @param d draw surface
     */
    private void drawMessage(DrawSurface d) {
        d.setColor(Color.BLACK);
        d.drawText(100, d.getHeight() / 2,
                "You Win! Your score is: " + score, 32);
    }
}