package animation;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;
import utils.Finals;

/**
 * An animation runner. Gets a specified animation and can run it.
 */
public class AnimationRunner {
    private Sleeper sleeper;
    private GUI gui;
    private double moveTimeSec;

    /**
     * Creates an animation runner.
     *
     * @param framesPerSecond num of frames to be changed per one second.
     */
    public AnimationRunner(int framesPerSecond) {
        this.sleeper = new Sleeper();
        this.gui = new GUI("Arkanoid", Finals.getInstance().getGameWidth(),
                Finals.getInstance().getGameHeight());
        this.moveTimeSec = 1.0 / framesPerSecond;
    }

    /**
     * Gets the gui instance.
     *
     * @return gui
     */
    public GUI getGui() {
        return gui;
    }

    /**
     * Runs the animation until it has to stop.
     *
     * @param animation the animation object
     */
    public void run(Animation animation) {
        // Run the animation until it decides to stop
        while (!animation.shouldStop()) {
            long startTime = System.currentTimeMillis(); // timing

            // Show one frame
            DrawSurface d = gui.getDrawSurface();
            animation.doOneFrame(d, moveTimeSec);
            gui.show(d);

            // Wait needed time
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep
                    = (long) (moveTimeSec * 1000) - usedTime;
            if (milliSecondLeftToSleep > 0) {
                this.sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }
}