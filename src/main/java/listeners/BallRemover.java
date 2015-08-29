package listeners;

import animation.GameLevel;
import sprites.ball.Ball;
import sprites.Block;
import utils.Counter;

/**
 * Class in charge of removing balls from the gameLevel, and keeping count of
 * the number of balls that were removed.
 */
public class BallRemover implements HitListener {
    private GameLevel gameLevel;
    private Counter ballsCount;

    /**
     * Constructs the block remover.
     *
     * @param gameLevelRef  reference to a gameLevel object
     * @param ballsCountRef counter of removed blocks
     */
    public BallRemover(GameLevel gameLevelRef, Counter ballsCountRef) {
        this.gameLevel = gameLevelRef;
        this.ballsCount = ballsCountRef;
    }

    /**
     * Removes it.
     *
     * @param beingHit the object that was hit
     * @param hitter   the ball that hit the object
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(this.gameLevel);
        this.ballsCount.decrease(1);
    }
}