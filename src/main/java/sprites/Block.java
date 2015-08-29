package sprites;

import animation.GameLevel;
import biuoop.DrawSurface;
import graphics.Point;
import graphics.Rectangle;
import listeners.HitListener;
import listeners.HitNotifier;
import sprites.ball.Ball;
import sprites.ball.Velocity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class represents a block - rectangle that a ball may collide.
 */
public class Block extends Rectangle
        implements Collidable, Sprite, HitNotifier {
    private int hitPoints;
    private List<HitListener> hitListeners;
    private Color strokeColor;
    private Fill defaultFill;
    private Fill currentFill;
    private Map<Integer, Fill> fills;

    /**
     * Creates a new block.
     *
     * @param upperLeft   upper left vertex
     * @param width       block width
     * @param height      block height
     * @param defaultFill default fill of the block
     * @param fills       different fills of the block by hit points
     * @param strokeColor the color of block's contour
     * @param hitPoints   start hit points
     */
    public Block(Point upperLeft, double width, double height, Fill defaultFill,
                 Map<Integer, Fill> fills, Color strokeColor,
                 int hitPoints) {
        super(upperLeft, width, height);
        this.hitPoints = hitPoints;
        this.hitListeners = new ArrayList<HitListener>();
        this.strokeColor = strokeColor;

        // Add size to the fills and save them
        this.defaultFill = defaultFill;
        this.fills = fills;
        updateCurrentFill();
    }

    /**
     * Creates a black block just by position and size.
     *
     * @param upperLeft position point
     * @param width     width of the block
     * @param height    height of the block
     */
    public Block(Point upperLeft, double width, double height) {
        super(upperLeft, width, height);
        this.hitPoints = -1;
        this.hitListeners = new ArrayList<HitListener>();
        this.strokeColor = Color.BLACK;
        this.defaultFill = new Fill(Color.BLACK);
        this.fills = new TreeMap<Integer, Fill>();
        updateCurrentFill();
    }

    /**
     * Updates the block fill according to current hit points.
     */
    private void updateCurrentFill() {
        // Check if fill specified for this point
        if (this.fills.containsKey(this.hitPoints)) {
            this.currentFill = this.fills.get(this.hitPoints);
        } else {
            // Fill not specified. Use default
            this.currentFill = this.defaultFill;
        }
    }

    /**
     * Return the "collision shape" of the object.
     *
     * @return collision shape
     */
    @Override
    public Rectangle getCollisionRectangle() {
        return this;
    }

    /**
     * Notify the object that we collided with a new velocity.
     *
     * @param collisionPoint point of collision
     * @param velocity       current velocity of the object
     * @return the new velocity expected after the hit (based on the force the
     * object inflicted on us).
     */
    @Override
    public Velocity hit(Point collisionPoint, Velocity velocity) {
        this.reduceScore();
        if (hitPoints > 0) {
            updateCurrentFill();
        }

        // Handle collision - update velocity of the hitter
        if (isLowerCorner(collisionPoint) || isUpperCorner(collisionPoint)) {
            handleCornerCollision(collisionPoint, velocity);
        } else {
            handleSideCollision(collisionPoint, velocity);
        }

        // Return the new velocity
        return velocity;
    }

    /**
     * Updates velocity if collided at corner.
     *
     * @param collisionPoint point of collision
     * @param velocity       current velocity of the object
     */
    private void handleCornerCollision(Point collisionPoint,
                                       Velocity velocity) {
        if (isLowerCorner(collisionPoint)) {
            if (velocity.getY() < 0) {
                velocity.reverseY(); // if moving up towards bottom
            } else {
                velocity.getX();     // if moving down towards the side
            }
        } else { // upper corner
            if (velocity.getY() > 0) {
                velocity.reverseY(); // if moving down towards the top
            } else {
                velocity.reverseX(); // if moving up towards the side
            }
        }
    }

    /**
     * Updates velocity if collided at side (not at corner!).
     *
     * @param collisionPoint point of collision
     * @param velocity       current velocity of the object
     */
    private void handleSideCollision(Point collisionPoint, Velocity velocity) {
        int leftX = (int) Math.round(getUpperLeft().getX());
        int rightX = leftX + (int) getWidth();
        int upperY = (int) Math.round(getUpperLeft().getY());
        int lowerY = upperY + (int) getHeight();
        int collisionX = (int) Math.round(collisionPoint.getX());
        int collisionY = (int) Math.round(collisionPoint.getY());

        // Check if collided at a side of the block
        if ((collisionX == leftX) || (collisionX == rightX)) {
            velocity.reverseX(); // change direction in axe X

            // Check if collided at top or bottom of the block
        } else if ((collisionY == upperY) || (collisionY == lowerY)) {
            velocity.reverseY(); // change direction in axe Y
        }
    }

    /**
     * Reduces the score of the block.
     */
    private void reduceScore() {
        if (this.hitPoints >= 0) {
            this.hitPoints--;
        }
    }

    /**
     * Draw the sprite to the screen.
     *
     * @param d the draw surface
     */
    @Override
    public void drawOn(DrawSurface d) {
        // Draw the contour if needed
        if (this.strokeColor != null) {
            d.setColor(this.strokeColor);
            d.drawRectangle((int) this.getUpperLeft().getX(),
                    (int) this.getUpperLeft().getY(), (int) this.getWidth(),
                    (int) this.getHeight());
        }

        // Fill the block
        this.currentFill.fillRectangle(d, this);
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
     * Adds the block to the game.
     *
     * @param g game
     */
    @Override
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * Removes the block from the gameLevel.
     *
     * @param gameLevel the gameLevel object to remove from
     */
    public void removeFromGame(GameLevel gameLevel) {
        gameLevel.removeCollidable(this);
        gameLevel.removeSprite(this);
    }

    /**
     * Adds listener to hit events.
     *
     * @param hl the listener to add
     */
    @Override
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     * Removes the listener from list.
     *
     * @param hl the listener to be removed
     */
    @Override
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * Notifies all the listeners that this block was hit.
     *
     * @param hitter the ball that hit the block
     */
    public void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners =
                new ArrayList<HitListener>(this.hitListeners);

        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * Returns the hut points number.
     *
     * @return number of hit points
     */
    public int getHitPoints() {
        return this.hitPoints;
    }
}
