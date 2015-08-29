package sprites.statusbar;

import animation.GameLevel;
import sprites.Sprite;
import utils.Finals;

/**
 * Represents a status sprites.statusbar.Indicator - a sprite on the status bar.
 */
public abstract class Indicator implements Sprite {
    protected static final int START_Y
            = Finals.getInstance().getStatusBarHeight() - 2;
    protected static final int TEXT_SIZE = 18;
    protected static final java.awt.Color TEXT_COLOR = java.awt.Color.BLACK;
    private int width = 0;
    private int xPosition;

    /**
     * Returnes the indicator's width.
     *
     * @return the indicator's width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the indicator's width.
     *
     * @param newWidth the indicator's width.
     */
    public void setWidth(int newWidth) {
        this.width = newWidth;
    }

    /**
     * Gets the start position at axe X.
     *
     * @return start position at axe x
     */
    public int getX() {
        return this.xPosition;
    }

    /**
     * Sets the start position at axe X.
     *
     * @param x start position at axe x
     */
    public void setX(int x) {
        this.xPosition = x;
    }

    /**
     * Notify the sprite that time has passed.
     *
     * @param dt time passed since last invocation
     */
    @Override
    public void timePassed(double dt) {
    }

    /**
     * Adds the sprite to the game.
     *
     * @param g game
     */
    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }
}
