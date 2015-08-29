package sprites;

import animation.GameLevel;
import biuoop.DrawSurface;

/**
 * This interface represents a sprites.Sprite - simple object on game field.
 */
public interface Sprite {
    /**
     * Draw the sprite to the screen.
     *
     * @param d the draw surface
     */
    void drawOn(DrawSurface d);

    /**
     * Notify the sprite that time has passed.
     *
     * @param dt time passed since last invocation
     */
    void timePassed(double dt);

    /**
     * Adds the sprite to the game.
     *
     * @param g game
     */
    void addToGame(GameLevel g);
}