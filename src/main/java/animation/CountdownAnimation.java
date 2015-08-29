package animation;

import biuoop.DrawSurface;
import gameplay.SpriteCollection;

import java.awt.Color;

/**
 * Represents the countdown animation before the game begins.
 */
public class CountdownAnimation implements Animation {
    private SpriteCollection gameScreen;
    private int currNumber;
    private double secPerNumber;
    private double currSecPassed;

    /**
     * Creates the countdown animation.
     *
     * @param numOfSeconds seconds to hold the animation
     * @param countFrom    number to count from to 1
     * @param sprites      the screen to display
     */
    public CountdownAnimation(double numOfSeconds, int countFrom,
                              SpriteCollection sprites) {
        this.gameScreen = sprites;
        this.currNumber = countFrom;
        this.secPerNumber = numOfSeconds / countFrom;
        this.currSecPassed = 0;
    }

    /**
     * Prepare a simple animation step.
     *
     * @param d           is the surface
     * @param moveTimeSec is the time passed since last move
     */
    public void doOneFrame(DrawSurface d, double moveTimeSec) {
        currSecPassed += moveTimeSec;

        // If time passed - next number
        if (currSecPassed >= this.secPerNumber) {
            currSecPassed = 0;
            currNumber--;
        }

        gameScreen.drawAllOn(d); // draw the game screen
        drawCurrNumber(d); // draw the countdown number
    }

    /**
     * Draws the countdown number.
     *
     * @param d draw surface
     */
    private void drawCurrNumber(DrawSurface d) {
        // See what number to draw
        String numToDraw = String.valueOf(currNumber);
        if (currNumber == 0) {
            numToDraw = "1";
        }

        d.setColor(Color.BLACK);
        int xMiddle = d.getWidth() / 2;
        int yMiddle = d.getHeight() / 2;
        d.drawText(xMiddle, yMiddle, numToDraw, 80);
        d.setColor(new Color(248, 255, 46));
        d.drawText(xMiddle + 4, yMiddle - 4, numToDraw, 70);
    }

    /**
     * Says if the animation should stop.
     *
     * @return true / false
     */
    public boolean shouldStop() {
        return this.currNumber == 0;
    }
}