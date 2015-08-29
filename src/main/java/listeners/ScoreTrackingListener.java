package listeners;

import sprites.ball.Ball;
import sprites.Block;
import utils.Counter;

/**
 * Counts the player's score.
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
     * Create a score tracking listener.
     *
     * @param scoreCounter reference to counter of player's score
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    /**
     * Adds the proper amount of points to player when ball hits a block.
     *
     * @param beingHit the object that was hit
     * @param hitter   the ball that hit the object
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        // If the block was destroyed
        if (beingHit.getHitPoints() == 0) {
            this.currentScore.increase(10);

            // If the block was hit
        } else if (beingHit.getHitPoints() > 0) {
            this.currentScore.increase(5);
        }

    }
}