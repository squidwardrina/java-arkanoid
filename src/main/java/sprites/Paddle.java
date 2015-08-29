package sprites;

import animation.GameLevel;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import graphics.Point;
import graphics.Rectangle;
import sprites.ball.Velocity;
import utils.Finals;

/**
 * sprites.Paddle - the player in the game.
 */
public final class Paddle extends Rectangle implements Sprite, Collidable {
    // Constants
    private static final java.awt.Color FILL_COLOR = java.awt.Color.ORANGE;
    private static final java.awt.Color BORDER_COLOR = java.awt.Color.DARK_GRAY;

    // Properties
    private static Paddle instance;
    private biuoop.KeyboardSensor keyboard;
    private double step;

    /**
     * Create a new paddle with the keyboard sensor.
     *
     * @param keyboard keyboard sensor
     * @param step     paddle's speed
     * @param width    paddle's width
     */
    private Paddle(KeyboardSensor keyboard, double step, double width) {
        super(new Point(
                Math.round(Finals.getInstance().getGameWidth() / 2 - width / 2),
                Math.round(Finals.getInstance().getPaddleYStart())), width,
                Finals.getInstance().getPaddleHeight(), Paddle.FILL_COLOR);
        this.keyboard = keyboard;
        this.step = step;
    }

    /**
     * Returns a paddle with new width and speed. If doesn't exist - create it.
     *
     * @param keyboard keyboard sensor
     * @param speed    paddle's speed
     * @param width    paddle's width
     * @return the paddle
     */
    public static Paddle getInstance(KeyboardSensor keyboard, double speed,
                                     double width) {
        if (Paddle.instance == null) { // create if doesn't exist
            Paddle.instance = new Paddle(keyboard, speed, width);
        } else {
            Paddle.instance.setStep(speed);
            Paddle.instance.setWidth(width);
            Paddle.instance.moveToDefault();
        }
        return instance;
    }

    /**
     * Sets paddle speed.
     *
     * @param newStep new speed
     */
    private void setStep(double newStep) {
        this.step = newStep;
    }

    /**
     * Moves the paddle to default place.
     */
    public void moveToDefault() {
        this.getUpperLeft()
                .setX(Math.round(Finals.getInstance().getGameWidth() / 2
                        - Paddle.instance.getWidth() / 2));
    }

    /**
     * Draws the paddle on surface d.
     *
     * @param d the draw surface
     */
    @Override
    public void drawOn(DrawSurface d) {
        // Fill the rectangle
        d.setColor(Paddle.FILL_COLOR);
        d.fillRectangle((int) this.getUpperLeft().getX(),
                (int) this.getUpperLeft().getY(),
                (int) Paddle.instance.getWidth(), (int) this.getHeight());

        // Draw a border
        d.setColor(Paddle.BORDER_COLOR);
        d.drawRectangle((int) this.getUpperLeft().getX(),
                (int) this.getUpperLeft().getY(),
                (int) Paddle.instance.getWidth(), (int) this.getHeight());
    }

    /**
     * Notify the sprite that time has passed.
     *
     * @param dt time passed since last invocation
     */
    public void timePassed(double dt) {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            this.moveLeft(dt);
        } else if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            this.moveRight(dt);
        }
    }

    /**
     * Adds the paddle to the game.
     *
     * @param g game
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * Moves right.
     *
     * @param moveTimeSec seconds for move
     */
    public void moveRight(double moveTimeSec) {
        Finals finals = Finals.getInstance();
        double speed = step * moveTimeSec;
        Point pos = getUpperLeft();
        int gameWidth = finals.getGameWidth() - finals.getMargin();

        // Make sure not to pass the screen borders
        if ((pos.getX() + getWidth() + speed) > gameWidth) {
            pos.setX(gameWidth - getWidth());
        } else {
            pos.setX((pos.getX() + speed));
        }
    }

    /**
     * Moves left.
     *
     * @param moveTimeSec seconds for move
     */
    public void moveLeft(double moveTimeSec) {
        double speed = (this.step * moveTimeSec);
        Finals finals = Finals.getInstance();
        Point pos = getUpperLeft();

        // Make sure not to pass the screen borders
        if ((pos.getX() - speed) < finals.getMargin()) {
            pos.setX(finals.getMargin());
        } else {
            pos.setX(pos.getX() - speed);
        }
    }

    /**
     * Gets the paddle.
     *
     * @return the paddle
     */
    public Rectangle getCollisionRectangle() {
        return new Rectangle(getUpperLeft(), getWidth(), 1);
    }

    /**
     * Notify the object that we collided with a new velocity.
     *
     * @param collisionPoint point of collision
     * @param velocity       current velocity of the object
     * @return the new velocity expected after the hit (based on the force the
     * object inflicted on us).
     */
    public Velocity hit(Point collisionPoint, Velocity velocity) {
        // Create shortcuts for simplicity
        int collisionX = (int) Math.round(collisionPoint.getX());
        int collisionY = (int) Math.round(collisionPoint.getY());
        int leftSide = (int) Math.round(getUpperLeft().getX());
        int rightSide = (int) Math.round(leftSide + getWidth());
        int top = (int) Math.round(getUpperLeft().getY());
        int bottom = (int) Math.round(getUpperLeft().getY() + getWidth());
        boolean upperCorner = isUpperCorner(collisionPoint);
        boolean goingUp = velocity.getY() < 0;
        boolean goingDown = velocity.getY() > 0;

        // If collided at the top of the paddle
        if ((collisionY == top) || (upperCorner && goingDown)) {
            // Get the new angle to give the collided object
            double newAngle = getNewAngle(collisionX);
            if (newAngle == 0) {
                velocity.reverseY(); // if no need to change the angle
            } else {
                velocity.setAngle(newAngle); // set new angle
            }

            // If collided at a side or bottom
        } else if ((collisionX == leftSide) || (collisionX == rightSide)
                || (upperCorner && goingUp) || (collisionY == bottom)) {
            velocity.reverseX(); // change direction in axe X

        }
        return velocity;
    }

    /**
     * Gets the angle change depending on paddle region.
     *
     * @param pointX point on the paddle
     * @return angle change
     */
    private double getNewAngle(double pointX) {
        final int regionsNum = 5;
        double regionSize = getWidth() / regionsNum;
        double currRegEnd;
        double angle = Math.toRadians(-60);
        double dAngle = Math.toRadians(30);

        // Go over the regions until got to the point
        for (int i = 1; i < regionsNum; i++) {
            currRegEnd = getUpperLeft().getX() + i * regionSize;
            if (pointX <= currRegEnd) {
                return angle + (i - 1) * dAngle; // return proper angle change
            }
        }

        // Must be in last region
        return angle + (regionsNum - 1) * dAngle; // return proper angle change
    }
}