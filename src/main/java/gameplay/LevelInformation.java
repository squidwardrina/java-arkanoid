package gameplay;

import sprites.ball.Velocity;
import sprites.Block;
import sprites.Sprite;

import java.util.List;

/**
 * Information about a certain level.
 */
public interface LevelInformation {
    /**
     * Returns the number of balls for the level.
     *
     * @return balls number
     */
    int numberOfBalls();

    /**
     * The initial velocities for each ball.
     *
     * @return balls' velocities.
     */
    List<Velocity> initialBallVelocities();

    /**
     * sprites.Paddle's peed.
     *
     * @return speed of the paddle for the level
     */
    int paddleSpeed();

    /**
     * sprites.Paddle's with.
     *
     * @return width of the paddle for the level
     */
    int paddleWidth();

    /**
     * Name of the level.
     *
     * @return level's name
     */
    String levelName();

    /**
     * Returns a sprite with the background of the level.
     *
     * @return a sprite with the background of the level
     */
    Sprite getBackground();

    /**
     * The Blocks that make up this level. Each block contains it's size, color
     * and location.
     *
     * @return blocks
     */
    List<Block> blocks();

    /**
     * Number of blocks to remove before the level is cleared. This number is <=
     * blocks.size()
     *
     * @return blocks to remove
     */
    int numberOfBlocksToRemove();
}
