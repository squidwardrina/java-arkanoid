package sprites;

import animation.GameLevel;
import sprites.ball.Velocity;
import graphics.Point;
import graphics.Rectangle;

/**
 * An object that can be collided with.
 */
public interface Collidable {
    /**
     * Return the "collision shape" of the object.
     *
     * @return collision shape
     */
    Rectangle getCollisionRectangle();

    /**
     * Notify the object that we collided with a new velocity.
     *
     * @param collisionPoint  point of collision
     * @param currentVelocity current velocity of the object
     * @return the new velocity expected after the hit (based on the force the
     * object inflicted on us).
     */
    Velocity hit(Point collisionPoint, Velocity currentVelocity);

    /**
     * Adds the sprite to the game.
     *
     * @param g game
     */
    void addToGame(GameLevel g);
}