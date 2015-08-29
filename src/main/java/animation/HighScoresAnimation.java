package animation;

import biuoop.DrawSurface;
import scores.HighScoresTable;
import scores.ScoreInfo;

import java.awt.Color;

/**
 * Represents the high scores screen animations.
 */
public class HighScoresAnimation implements Animation {
    private HighScoresTable scores;

    /**
     * Create a high scores animation screen.
     *
     * @param scores scores table
     */
    public HighScoresAnimation(HighScoresTable scores) {
        this.scores = scores;
    }

    /**
     * One animation step.
     *
     * @param d  is the surface
     * @param dt is the time of the move
     */
    public void doOneFrame(DrawSurface d, double dt) {
        drawBackground(d);
        if (scores == null) {
            showNoScores(d);
        } else {
            drawMessage(d);
        }
    }

    /**
     * Draw the message we have no scores yet.
     *
     * @param d draw surface
     */
    private void showNoScores(DrawSurface d) {
        int startX = 200;
        int startY = 200;

        d.setColor(java.awt.Color.BLUE);
        d.drawText(startX, startY, "No scores yet.", 40);
    }

    /**
     * Draw the scores on the screen.
     *
     * @param d draw surface
     */
    private void drawMessage(DrawSurface d) {
        int startX = 200;
        int startY = 200;

        // Go over the scores and print them to the screen
        for (ScoreInfo scoreInfo : this.scores.getHighScores()) {
            startY += 60;
            d.setColor(java.awt.Color.BLUE);
            d.drawText(startX, startY, scoreInfo.getName() + ".........."
                    + scoreInfo.getScore(), 40);
        }
    }

    /**
     * Draws the background of the screen.
     *
     * @param d draw surface
     */
    private void drawBackground(DrawSurface d) {
        // Draw the screen and set the color
        d.setColor(new Color(240, 225, 178));
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());

        // The first medal
        d.setColor(Color.BLACK);
        d.drawLine(0, 0, 130, 280);
        d.drawLine(5, 0, 135, 280);
        d.drawLine(200, 0, 70, 280);
        d.drawLine(195, 0, 65, 280);
        d.setColor(new Color(0, 126, 178));
        d.fillCircle(100, 280, 75);
        d.setColor(new Color(178, 232, 255));
        d.drawText(70, 295, "#1", 50);

        // The second medal
        d.setColor(Color.BLACK);
        d.drawLine(340, 0, 425, 120);
        d.drawLine(345, 0, 430, 120);
        d.drawLine(460, 0, 375, 120);
        d.drawLine(455, 0, 370, 120);
        d.setColor(new Color(229, 0, 140));
        d.fillCircle(400, 120, 55);
        d.setColor(new Color(225, 152, 215));
        d.drawText(380, 130, "#1", 35);

        // The third medal
        d.setColor(Color.BLACK);
        d.drawLine(600, 0, 720, 350);
        d.drawLine(605, 0, 725, 350);
        d.drawLine(800, 0, 660, 350);
        d.drawLine(795, 0, 655, 350);
        d.setColor(new Color(100, 0, 127));
        d.fillCircle(700, 340, 80);
        d.setColor(new Color(233, 153, 255));
        d.drawText(670, 355, "#1", 50);
    }

    /**
     * Indicates whether the animation should stop or not.
     *
     * @return true / false
     */
    public boolean shouldStop() {
        return true;
    }
}