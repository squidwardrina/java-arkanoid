package sprites;

import animation.GameLevel;
import biuoop.DrawSurface;

/**
 * Represents a background of the game as sprite.
 */
public class Background implements Sprite {
    private Fill fill;

    /**
     * Creates new background sprite.
     *
     * @param fill the background filling
     */
    public Background(Fill fill) {
        this.fill = fill;
    }

    /**
     * Draw all the shapes of the background.
     *
     * @param d the draw surface
     */
    public void drawOn(DrawSurface d) {
        this.fill.fillSurface(d);
    }

    /**
     * Notify the sprite that time has passed.
     *
     * @param dt time passed since last invocation
     */
    public void timePassed(double dt) {

    }

    /**
     * Adds the sprite to the game.
     *
     * @param g game
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }
}
