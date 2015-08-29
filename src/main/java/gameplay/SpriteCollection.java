package gameplay;

import biuoop.DrawSurface;
import sprites.Sprite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Collection of all the sprites on the game field.
 */
public class SpriteCollection {
    private LinkedList<Sprite> sprites;

    /**
     * Creates an empty sprite collection.
     */
    public SpriteCollection() {
        sprites = new LinkedList<Sprite>();
    }

    /**
     * Adds a sprite to collection.
     *
     * @param s new sprite
     */
    public void addSprite(Sprite s) {
        sprites.add(s);
    }

    /**
     * Removes a sprite from collection.
     *
     * @param s sprite to remove
     */
    public void removeSprite(Sprite s) {
        sprites.remove(s);
    }

    /**
     * Notifies all the sprites that a time has passed.
     *
     * @param dt time passed in seconds
     */
    public void notifyAllTimePassed(double dt) {
        // Copy the sprites collection before iterating, to prevent exceptions
        // in case the list changes while iterating
        List<Sprite> spritesCopy = new ArrayList<Sprite>(sprites);

        // Notify the sprites time passed
        for (Sprite sprite : spritesCopy) {
            sprite.timePassed(dt);
        }
    }

    /**
     * Draw all the sprites on d.
     *
     * @param d draw surface
     */
    public void drawAllOn(DrawSurface d) {
        // Copy the sprites collection before iterating, to prevent exceptions
        // in case the list changes while iterating
        List<Sprite> spritesCopy = new ArrayList<Sprite>(sprites);

        // Draw all the sprites
        for (Sprite sprite : spritesCopy) {
            sprite.drawOn(d);
        }
    }
}