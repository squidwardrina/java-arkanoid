package graphics;

/**
 * This class represents a point.
 */
public class Point {
    private double x; // the X coordinate of the point
    private double y; // the Y coordinate of the point

    /**
     * This is a constructor.
     *
     * @param x is x coordinate of the point
     * @param y is y coordinate of the point
     */
    public Point(double x, double y) {
        this.x = Math.round(x);
        this.y = Math.round(y);
    }

    /**
     * Return the distance between two points.
     *
     * @param other is the point we compare to
     * @return the distance
     */
    public double distance(Point other) {
        return (Math.sqrt(((this.x - other.getX()) * (this.x - other.getX()))
                + ((this.y - other.getY()) * (this.y - other.getY()))));
    }

    /**
     * Return the x value of this point.
     *
     * @return x
     */
    public double getX() {
        return (this.x);
    }

    /**
     * Return the y value of this point.
     *
     * @return y
     */
    public double getY() {
        return (this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // if it's the same object
        }
        if (o == null || getClass() != o.getClass()) {
            return false; // if it's empty or different type
        }

        Point point = (Point) o; // validated. cast to point

        // Compare
        if (Math.round(point.x) != Math.round(this.x)) {
            return false;
        }
        if (Math.round(point.y) != Math.round(this.y)) {
            return false;
        }

        return true; // success - points are equal
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Set the newX coordinate.
     *
     * @param newX is newX coordinate
     */
    public void setX(double newX) {
        this.x = newX;
    }

    /**
     * Set the newY coordinate.
     *
     * @param newY is newY coordinate
     */
    public void setY(double newY) {
        this.y = newY;
    }
}