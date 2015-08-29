package sprites.ball;

import graphics.Point;

/**
 * Velocity specifies the change in position on the `x` and the `y` axes.
 */
public class Velocity {
    private double dx;
    private double dy;

    /**
     * Constructs velocity.
     *
     * @param dx change in x axe
     * @param dy change in y axe
     */
    public Velocity(double dx, double dy) {
        this.dx = Math.round(Math.round(dx * 100) / 100);
        this.dy = Math.round(Math.round(dy * 100) / 100);
    }

    /**
     * Sets velocity by angle and speed.
     *
     * @param angle angle of movement
     * @param speed speed of the object
     * @return object's velocity - dx and dy
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double radAngle = Math.toRadians(angle - 90);
        double dx = Math.cos(radAngle) * speed;
        double dy = Math.sin(radAngle) * speed;
        return new Velocity(dx, dy);
    }

    /**
     * Gets velocity in X axe.
     *
     * @return dx
     */
    public double getX() {
        return Math.round(dx);
    }

    /**
     * Gets velocity in Y axe.
     *
     * @return dy
     */
    public double getY() {
        return Math.round(dy);
    }

    /**
     * Reverses the velocity in X axe.
     */
    public void reverseX() {
        this.dx = -Math.round(dx);
    }

    /**
     * Reverses the velocity in Y axe.
     */
    public void reverseY() {
        this.dy = -Math.round(dy);
    }

    /**
     * Applies the velocity to a point: (x, y) -> (x + dx, y + dy).
     *
     * @param p       point (x, y)
     * @param seconds part of second made in one move
     * @return new point (x + dx, y + dy)
     */
    public Point applyToPoint(Point p, double seconds) {
        return new Point(Math.round(p.getX() + (dx * seconds)),
                Math.round(p.getY() + (dy * seconds)));
    }

    /**
     * Changes speed vector's angle.
     *
     * @param newAngle a needed angle (0 = up)
     */
    public void setAngle(double newAngle) {
        dx = Math.round(Math.sin(newAngle) * getSpeed() * 100) / 100;
        dy = Math.round(-Math.cos(newAngle) * getSpeed() * 100) / 100;
    }

    /**
     * Gets the speed vector's size.
     *
     * @return the speed
     */
    public double getSpeed() {
        // Pythagorean theorem
        return Math.round(Math.sqrt(dx * dx + dy * dy));
    }
}
