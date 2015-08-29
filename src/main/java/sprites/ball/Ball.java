package sprites.ball;

import animation.GameLevel;
import biuoop.DrawSurface;
import graphics.Line;
import graphics.Point;
import gameplay.GameEnvironment;
import sprites.Block;
import sprites.Collidable;
import sprites.Sprite;

import java.awt.Color;

/**
 * This class represents a ball.
 */
public class Ball implements Sprite {
    private Point center;
    private int radius;
    private Color color;
    private Velocity velocity;
    private GameEnvironment environment;

    /**
     * Constructs a ball.
     *
     * @param center ball's center point
     * @param r      ball's radius
     * @param color  ball's color
     */
    public Ball(Point center, int r, Color color) {
        this.center = new Point(center.getX(), center.getY());
        this.radius = r;
        this.color = color;
        this.velocity = new Velocity(0, 0);
    }

    /**
     * Gets x value of ball's center.
     *
     * @return x
     */
    public int getX() {
        return (int) Math.round(center.getX());
    }

    /**
     * Returns Y coordinate of ball's center.
     *
     * @return y
     */
    public int getY() {
        return (int) Math.round(center.getY());
    }

    /**
     * Returns ball's color.
     *
     * @return ball's color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Draws the ball on the given DrawSurface.
     *
     * @param d the d to draw on
     */
    public void drawOn(DrawSurface d) {
        d.setColor(color);
        d.fillCircle(getX(), getY(), radius);
        d.setColor(Color.BLACK);
        d.drawCircle(getX(), getY(), radius);
    }

    /**
     * Notify the sprite that time has passed.
     *
     * @param dt time passed since last invocation
     */
    public void timePassed(double dt) {
        moveOneStep(dt);
    }

    /**
     * Adds the ball to the game.
     *
     * @param g game
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }

    /**
     * Removes the ball from the gameLevel.
     *
     * @param gameLevel the gameLevel object to remove from
     */
    public void removeFromGame(GameLevel gameLevel) {
        gameLevel.removeSprite(this);
    }

    /**
     * Sets velocity to the ball.
     *
     * @param dx change in x axe
     * @param dy change in y axe
     */
    public void setVelocity(double dx, double dy) {
        velocity = new Velocity(dx, dy);
    }

    /**
     * Move the ball one step.
     *
     * @param secPerMove time for one move
     */
    public void moveOneStep(double secPerMove) {

        // Check for collision and get the closest one
        Line trajectory = getTrajectory(secPerMove);
        CollisionInfo collision = getFirstCollision(trajectory);

        // If no collision - move tha ball to needed place
        if (collision == null) {
            center = velocity.applyToPoint(center, secPerMove);
        } else {
            handleCollision(collision);
        }
    }

    /**
     * Sets the center position.
     *
     * @param x x coord
     * @param y y coord
     */
    public void setCenter(double x, double y) {
        center = new Point(x, y);
    }

    /**
     * Handles collision with another object.
     *
     * @param collision the collision info
     */
    private void handleCollision(CollisionInfo collision) {
        Point collisionPoint = collision.collisionPoint();
        Collidable collisionObject = collision.collisionObject();
        double smallDx = velocity.getX() / 100;
        double smallDy = velocity.getY() / 100;
        double collisionX = collisionPoint.getX();
        double collisionY = collisionPoint.getY();


        // Move the ball slightly before the collision point
        setCenter(collisionX - smallDx, collisionY - smallDy);

        // Update ball's velocity
        velocity = collisionObject.hit(collisionPoint, velocity);

        // Notify the object that it was hit if needed
        if (collisionObject instanceof Block) {
            ((Block) collisionObject).notifyHit(this);
        }
    }

    /**
     * Gets the trajectory of the ball from start point to next step.
     *
     * @param secPerMove seconds per move to apply velocity
     * @return trajectory
     */
    private Line getTrajectory(double secPerMove) {
        return new Line(center, velocity.applyToPoint(center, secPerMove));
    }

    /**
     * Get the collision info by the trajectory.
     *
     * @param trajectory the line to check collision
     * @return Gets collision with the closest object, null if no collision
     */
    private CollisionInfo getFirstCollision(Line trajectory) {
        return environment.getClosestCollision(trajectory);
    }

    /**
     * Sets the game environment.
     *
     * @param gameEnvironment game board
     */
    public void setEnvironment(GameEnvironment gameEnvironment) {
        this.environment = gameEnvironment;
    }
}