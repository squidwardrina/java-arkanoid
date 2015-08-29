package gameplay;

import sprites.ball.CollisionInfo;
import biuoop.DrawSurface;
import graphics.Line;
import graphics.Point;
import graphics.Rectangle;
import sprites.Block;
import sprites.Collidable;

import java.util.LinkedList;
import java.util.List;

/**
 * The game environment - contains all the objects on the game screen.
 */
public class GameEnvironment {
    private LinkedList<Collidable> collidables;

    /**
     * Creates the game environment and adds blocks on screen sides.
     */
    public GameEnvironment() {
        this.collidables = new LinkedList<Collidable>();
    }

    /**
     * Add a collidable to the environment.
     *
     * @param c collidable object
     */
    public void addCollidable(Collidable c) {
        this.collidables.add(c);
    }

    /**
     * Removes the collidable from the list.
     *
     * @param c collidable to remove
     */
    public void removeCollidable(Collidable c) {
        this.collidables.remove(c);
    }

    /**
     * Draws all the game blocks on surface d.
     *
     * @param d draw surface
     */
    public void drawGameBlocks(DrawSurface d) {
        // Make a copy to prevent exceptions if the list changes
        List<Collidable> collidablesCopy
                = new LinkedList<Collidable>(this.collidables);
        for (Collidable block : collidablesCopy) {
            ((Block) block).drawOn(d);
        }
    }

    /**
     * Get the collision info by the track line.
     *
     * @param trajectory the line to check collision
     * @return Gets collision with the closest object, null if no collision
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        double minDistance = -1;
        double currDistance;
        Point currIntersection;
        Point closestIntersection = null;
        Collidable closestObject = null;

        // Make a copy to prevent exceptions if the list changes
        List<Collidable> collidablesCopy
                = new LinkedList<Collidable>(this.collidables);

        // Go over all the collidables
        for (Collidable collidable : collidablesCopy) {
            Rectangle rectangle = collidable.getCollisionRectangle();
            // Get the intersection point with current rectangle
            currIntersection = trajectory
                    .closestIntersectionToStartOfLine(rectangle);
            if (currIntersection == null) {
                continue;
            }

            // If it's the closest point yet - save it
            currDistance = trajectory.start().distance(currIntersection);

            if (currDistance == 0) { // if it's the same point - don't count it
                continue;
            }
            if ((minDistance == -1) || (currDistance < minDistance)) {
                minDistance = currDistance;
                closestIntersection = currIntersection;
                closestObject = collidable;
            }
        }

        if (closestIntersection != null) {
            // Round the coordinates and return
            double roundX = Math.round(closestIntersection.getX());
            double roundY = Math.round(closestIntersection.getY());
            closestIntersection.setX(roundX);
            closestIntersection.setY(roundY);
            return new CollisionInfo(closestIntersection, closestObject);
        }
        return null; // no collision
    }

}
