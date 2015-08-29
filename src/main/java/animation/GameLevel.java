package animation;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import gameplay.GameEnvironment;
import gameplay.LevelInformation;
import gameplay.SpriteCollection;
import graphics.Point;
import listeners.BallRemover;
import listeners.BlockRemover;
import listeners.ScoreTrackingListener;
import sprites.Block;
import sprites.Collidable;
import sprites.Wall;
import sprites.Sprite;
import sprites.Paddle;
import sprites.ball.Ball;
import sprites.ball.Velocity;
import sprites.statusbar.Indicator;
import sprites.statusbar.ScoreIndicator;
import sprites.statusbar.LevelIndicator;
import sprites.statusbar.LivesIndicator;
import sprites.statusbar.StatusBar;
import utils.Counter;
import utils.Finals;

import java.util.ArrayList;

/**
 * This class holds all the sprites and is in charge of animation.
 */
public class GameLevel implements Animation {
    private AnimationRunner runner;
    private boolean running;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private KeyboardSensor keyboard;
    private Counter blocksToRemove;
    private Counter ballsCount;
    private Counter playerScore;
    private Counter livesLeft;
    private LevelInformation levelInfo;

    /**
     * Creates the game level.
     *
     * @param levelInfo      current level information
     * @param keyboardSensor keyboard sensor of the game
     * @param runner         the game's animation runner
     * @param score          user's current score
     * @param livesLeft      player's lives left
     */
    public GameLevel(LevelInformation levelInfo, KeyboardSensor keyboardSensor,
                     AnimationRunner runner, Counter score, Counter livesLeft) {
        // Current level properties
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.blocksToRemove = new Counter(levelInfo.numberOfBlocksToRemove());
        this.ballsCount = new Counter(levelInfo.numberOfBalls());
        this.levelInfo = levelInfo;

        // Game properties
        this.runner = runner;
        this.keyboard = keyboardSensor;
        this.playerScore = score;
        this.livesLeft = livesLeft;
    }

    /**
     * Adds a collidable to collection.
     *
     * @param c new collidable
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * Initialize a new game. Creates blocks, ball and paddle and adds them to
     * game
     */
    public void initialize() {
        levelInfo.getBackground().addToGame(this); // add the background
        createStatusBar();
        createBorders();
        addLevelBlocks();
    }

    /**
     * Adds the blocks to the game and registers listeners for hits.
     */
    private void addLevelBlocks() {
        for (Block block : levelInfo.blocks()) {
            // Add hit listeners to the block
            block.addHitListener(new BlockRemover(this, blocksToRemove));
            block.addHitListener(new ScoreTrackingListener(playerScore));

            // Add the block to game
            block.addToGame(this);
        }
    }

    /**
     * Creates borders of the screen.
     */
    private void createBorders() {
        Finals f = Finals.getInstance();
        Point topStart = new Point(0, f.getStatusBarHeight());
        int margin = f.getMargin();
        int gameHeight = f.getGameHeight();
        int gameWidth = f.getGameWidth();
        Point rightStart = new Point(
                gameWidth - margin, f.getStatusBarHeight());

        // Create walls on the screen sides
        new Wall(topStart, gameWidth, margin).addToGame(this);
        new Wall(topStart, margin, gameHeight).addToGame(this);
        new Wall(rightStart, margin, gameHeight).addToGame(this);

        // Create the death region - bottom wall. Place it below the screen
        Point deathPos = new Point(0, gameHeight + 50);
        Wall deathRegion = new Wall(deathPos, gameWidth, margin);
        deathRegion.addToGame(this);

        // Register ball remover listener to the death region
        BallRemover ballRemover = new BallRemover(this, ballsCount);
        deathRegion.addHitListener(ballRemover);
    }

    /**
     * Creates a status bar and all it's indicators.
     */
    private void createStatusBar() {
        // Create a list of indicators (in needed order!)
        ArrayList<Indicator> statusIndicators = new ArrayList<Indicator>();
        statusIndicators.add(new LivesIndicator(this.livesLeft));
        statusIndicators.add(new ScoreIndicator(this.playerScore));
        statusIndicators.add(new LevelIndicator(this.levelInfo.levelName()));

        // Create a status bar
        new StatusBar(statusIndicators).addToGame(this);
    }

    /**
     * Adds a sprite to collection.
     *
     * @param s sprite
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * Loads a level.
     */
    public void playOneTurn() {
        this.placeBallsAndPaddle();
        if (this.ballsCount.getValue() == 0) {
            this.ballsCount.increase(this.levelInfo.numberOfBalls());
        }

        // Count down
        this.runner.run(new CountdownAnimation(2, 3, this.sprites));

        // Run the game
        this.running = true;
        this.runner.run(this); // run the turn

        // No more balls - decrease lives count
        if (!this.areBallsLeft()) {
            this.livesLeft.decrease(1);

            // No more blocks - increase score
        } else if (!this.areBlocksLeft()) {
            this.playerScore.increase(100);
        }
    }

    /**
     * Places balls and paddle where needed.
     */
    private void placeBallsAndPaddle() {
        // Create the paddle
        createPaddle();

        // Create the balls
        Finals finals = Finals.getInstance();
        Point startBallPoint = new Point(finals.getGameWidth() / 2,
                finals.getPaddleYStart() - 10);
        for (Velocity velocity : this.levelInfo.initialBallVelocities()) {
            this.addNewBall(velocity, startBallPoint);
        }
    }

    /**
     * Create a ball and add it.
     *
     * @param velocity ball's velocity
     * @param start    the start point
     */
    public void addNewBall(Velocity velocity, Point start) {
        Ball ball = new Ball(start, 5, java.awt.Color.WHITE);
        ball.setEnvironment(this.environment); // send environment to the ball
        ball.setVelocity(velocity.getX(), velocity.getY());
        ball.addToGame(this);   // Add ball to game
    }

    /**
     * Creates a new paddle and adds it to the game.
     */
    private void createPaddle() {
        Paddle.getInstance(this.keyboard, this.levelInfo.paddleSpeed(),
                this.levelInfo.paddleWidth()).addToGame(this);
    }

    /**
     * Prepares one game frame.
     *
     * @param d  the game surface
     * @param dt seconds passed since last move
     */
    public void doOneFrame(DrawSurface d, double dt) {
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed(dt);

        // Check if we need to stop the game
        if (blocksToRemove.getValue() <= 0) {
            running = false;
        } else if (ballsCount.getValue() <= 0) {
            running = false;
        }

        // Check if we need to pause
        if (this.keyboard.isPressed("p")) {
            String stopKey = Finals.getInstance().getStopAnimationKey();

            // Create a pause screen wrapped with the stoppable animation
            KeyPressStoppableAnimation pauseScreen =
                    new KeyPressStoppableAnimation(this.keyboard,
                            new PauseScreen(), stopKey);

            // Run the pause screen animation
            this.runner.run(pauseScreen);
        }
    }

    /**
     * Returnes whether the game should stop.
     *
     * @return check result
     */
    public boolean shouldStop() {
        return !running;
    }

    /**
     * Removes the collidable from the collidables collection.
     *
     * @param c the collidable to remove
     */
    public void removeCollidable(Collidable c) {
        environment.removeCollidable(c);
    }

    /**
     * Removes the sprite from the sprites collection.
     *
     * @param s the sprite to remove
     */
    public void removeSprite(Sprite s) {
        sprites.removeSprite(s);
    }

    /**
     * Returns whether there are more blocks.
     *
     * @return true or false
     */
    public boolean areBlocksLeft() {
        return (blocksToRemove.getValue() > 0);
    }

    /**
     * Returns whether there are more balls.
     *
     * @return true or false
     */
    public boolean areBallsLeft() {
        return (ballsCount.getValue() > 0);
    }
}
