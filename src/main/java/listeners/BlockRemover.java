package listeners;

import animation.GameLevel;
import sprites.ball.Ball;
import sprites.Block;
import utils.Counter;

/**
 * Class in charge of removing blocks from the gameLevel, and keeping count of
 * the number of blocks that were removed.
 */
public class BlockRemover implements HitListener {
    private GameLevel gameLevel;
    private Counter blocksCount;

    /**
     * Constructs the block remover.
     *
     * @param gameLevelRef   reference to a gameLevel object
     * @param blocksCountRef counter of removed blocks
     */
    public BlockRemover(GameLevel gameLevelRef, Counter blocksCountRef) {
        this.gameLevel = gameLevelRef;
        this.blocksCount = blocksCountRef;
    }

    /**
     * If the hit block reached 0 hit points - remove it.
     *
     * @param beingHit the object that was hit
     * @param hitter   the ball that hit the object
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        if (beingHit.getHitPoints() == 0) {
            beingHit.removeHitListener(this);    // remove listener
            beingHit.removeFromGame(
                    this.gameLevel); // remove the block from gameLevel
            this.blocksCount.decrease(1);       // decrease the blocks count
        }
    }
}