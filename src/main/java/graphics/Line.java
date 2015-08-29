package graphics;

import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Represents a line.
 */
public class Line {
    private Point start;
    private Point end;

    /**
     * Constructs a line.
     *
     * @param start start point of the line
     * @param end   end point of the line
     */
    public Line(Point start, Point end) {
        this.start = new Point(start.getX(), start.getY());
        this.end = new Point(end.getX(), end.getY());
    }

    /**
     * Gets the start point of the line.
     *
     * @return the start point of the line
     */
    public Point start() {
        return (this.start);
    }

    /**
     * Gets the end point of the line.
     *
     * @return the end point of the line
     */
    public Point end() {
        return (this.end);
    }

    /**
     * Checks whether the line intersects with other line.
     *
     * @param other the line to check
     * @return true if the lines intersect, false otherwise
     */
    public boolean isIntersecting(Line other) {
        return Line2D.linesIntersect(this.start.getX(), this.start.getY(),
                this.end.getX(), this.end.getY(),
                other.start.getX(), other.start.getY(),
                other.end.getX(), other.end.getY());
    }

    /**
     * Gets the intersection point of lines.
     *
     * @param other the other line
     * @return the intersection point, null if they don't intersect
     */
    public Point intersectionWith(Line other) {
        // Check if lines intersect
        if (!this.isIntersecting(other)) {
            return null;
        }

        // Name the parameters - for convenience
        double thisX1 = this.start.getX();
        double thisX2 = this.end.getX();
        double thisY1 = this.start.getY();
        double thisY2 = this.end.getY();
        double otherX1 = other.start().getX();
        double otherX2 = other.end().getX();
        double otherY1 = other.start().getY();
        double otherY2 = other.end().getY();

        // Say a line is defined by an equation: a*x + b*y = c.

        // Get our line's equation
        double a1 = thisY2 - thisY1;
        double b1 = thisX1 - thisX2;
        double c1 = a1 * thisX1 + b1 * thisY1;

        // Get other line's equation
        double a2 = otherY2 - otherY1;
        double b2 = otherX1 - otherX2;
        double c2 = a2 * otherX1 + b2 * otherY1;

        // Get the intersection point
        double det = a1 * b2 - a2 * b1;
        if (det == 0) {
            return null; // Lines are parallel - return null
        }
        return new Point((b2 * c1 - b1 * c2) / det,
                (a1 * c2 - a2 * c1) / det);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // if it's the same line
        }

        // Check if the line is OK
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Line line = (Line) o; // convert to line

        // Compare the lines and return false if different
        return !(this.end != null ? !this.end.equals(line.end)
                : line.end != null)
                && !(this.start != null ? !this.start.equals(line.start)
                : line.start != null);

    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    /**
     * Finds the first intersection of the line with a given rectangle.
     *
     * @param rect rectangle
     * @return the first intersection with the rectangle
     */
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        Point result = null;

        // Get all intersections
        ArrayList<Point> intersections = getRectangleIntersections(rect);

        // Find the closest intersection
        double minDist = -1;
        double currDist;
        for (Point intersection : intersections) {
            currDist = intersection.distance(this.start());
            if ((minDist == -1) || (currDist < minDist)) {
                result = intersection;
                minDist = currDist;
            }
        }
        return result;
    }

    /**
     * Finds the intersections of the line with a given rectangle.
     *
     * @param rect rect
     * @return list of intersections
     */
    public ArrayList<Point> getRectangleIntersections(Rectangle rect) {
        // Find the rect's vertexes
        Point upperLeft = rect.getUpperLeft();
        Point upperRight = new Point(rect.getUpperLeft().getX()
                + rect.getWidth(), rect.getUpperLeft().getY());
        Point lowerLeft = new Point(rect.getUpperLeft().getX(),
                rect.getUpperLeft().getY() + rect.getHeight());
        Point lowerRight = new Point(rect.getUpperLeft().getX()
                + rect.getWidth(), rect.getUpperLeft().getY()
                + rect.getHeight());

        // Find the rect's sides
        Line upperLine = new Line(upperLeft, upperRight);
        Line lowerLine = new Line(lowerLeft, lowerRight);
        Line leftLine = new Line(upperLeft, lowerLeft);
        Line rightLine = new Line(upperRight, lowerRight);

        // Get intersections
        ArrayList<Point> intersections = new ArrayList<Point>(4);
        Point temp = this.intersectionWith(upperLine);
        if (temp != null) {
            intersections.add(temp);
        }
        temp = this.intersectionWith(lowerLine);
        if (temp != null) {
            intersections.add(temp);
        }
        temp = this.intersectionWith(leftLine);
        if (temp != null) {
            intersections.add(temp);
        }
        temp = this.intersectionWith(rightLine);
        if (temp != null) {
            intersections.add(temp);
        }

        return intersections;
    }
}