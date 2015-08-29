package animation;

import biuoop.DrawSurface;

/**
 * Class in charge of animation.
 */
public interface Animation {
    /**
     * One animation step.
     *
     * @param d  is the surface
     * @param dt is the time of the move
     */
    void doOneFrame(DrawSurface d, double dt);

    /**
     * Indicates whether the animation should stop or not.
     *
     * @return true / false
     */
    boolean shouldStop();
}