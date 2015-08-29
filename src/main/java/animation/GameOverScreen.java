package animation;

import biuoop.DrawSurface;
import graphics.Circle;
import graphics.Rectangle;

import java.awt.Color;

/**
 * Represents the pause screen.
 */
public class GameOverScreen implements Animation {
    private int score;

    /**
     * Create the end screen.
     *
     * @param finalScore is the final score
     */
    public GameOverScreen(int finalScore) {
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
     * Draws the text message to user.
     *
     * @param d draw surface
     */
    private void drawMessage(DrawSurface d) {
        d.setColor(Color.BLACK);
        d.drawText(100, d.getHeight() / 2,
                "Game Over. Your score is: " + score, 32);
    }

    /**
     * Draws the background of the screen.
     *
     * @param d draw surface
     */
    private void drawBackground(DrawSurface d) {
        new Rectangle(0, 0, d.getWidth(), d.getHeight(), Color.BLACK)
                .drawOn(d);
        new Rectangle(0, d.getHeight() / 2 - 30, d.getWidth(), 32, Color.WHITE)
                .drawOn(d);
        int xCenter = 600;
        int yCenter = 110;
        new Circle(xCenter, yCenter, 80, Color.WHITE, Color.YELLOW).drawOn(d);
        new Circle(xCenter - 10, yCenter - 10, 80, Color.BLACK, Color.BLACK)
                .drawOn(d);
    }

    /**
     * Checks if the animation should be stopped.
     *
     * @return check result
     */
    public boolean shouldStop() {
        return true;
    }
}