package listeners;

import sprites.ball.Ball;
import sprites.Block;

/**
 * The hit listener interface.
 */
public interface HitListener {
    /**
     * This method is called whenever the beingHit object is hit.
     *
     * @param beingHit the object that was hit
     * @param hitter   the ball that hit the object
     */
    void hitEvent(Block beingHit, Ball hitter);
}