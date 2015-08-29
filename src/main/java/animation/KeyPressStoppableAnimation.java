package animation;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

/**
 * Decorator wrapper - an animation that can be stopped by a key press.
 */
public class KeyPressStoppableAnimation implements Animation {
    private KeyboardSensor keyboardSensor;
    private String key;
    private Animation animation;
    private boolean stop = false;
    private boolean isAlreadyPressed = true;

    /**
     * Creates the wrapper.
     *
     * @param keyboardSensor a key sensor
     * @param animation      the animation type
     * @param key            the key that would stop the animation
     */
    public KeyPressStoppableAnimation(KeyboardSensor keyboardSensor,
                                      Animation animation, String key) {
        this.keyboardSensor = keyboardSensor;
        this.animation = animation;
        this.key = key;
    }

    /**
     * One animation step.
     *
     * @param d  is the surface
     * @param dt is the time of the move
     */
    public void doOneFrame(DrawSurface d, double dt) {
        stopIfNeeded();    // stops the animation if stop key was pressed
        this.animation.doOneFrame(d, dt);    // run one animation frame
    }

    /**
     * Stops the animation if stop key was pressed after the start.
     */
    private void stopIfNeeded() {
        // Check if the stop key is pressed
        if (this.keyboardSensor.isPressed(this.key)) {
            // If the stop key is pressed before animation started - ignore it
            if (!this.isAlreadyPressed) {
                this.stop = true;   // animation has to stop
            }
        } else {
            // The stop key was not pressed yet for this animation
            this.isAlreadyPressed = false;
        }
    }

    /**
     * Indicates whether the animation should stop or not.
     *
     * @return true / false
     */
    public boolean shouldStop() {
        boolean isToStop = stop;    // check if needs to stop
        stop = false;    // update the flag for future use not to stop
        return isToStop;
    }
}

