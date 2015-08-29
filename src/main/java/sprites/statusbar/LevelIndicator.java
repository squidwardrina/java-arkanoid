package sprites.statusbar;

import biuoop.DrawSurface;

/**
 * Represents a level indicator at the status bar.
 */
public class LevelIndicator extends Indicator {
    private String levelName;

    /**
     * Creates a level sprites.statusbar.Indicator.
     *
     * @param levelName level name
     */
    public LevelIndicator(String levelName) {
        this.levelName = levelName;
        setWidth(400);
        this.setX(0); // just to init
    }

    /**
     * Draw the sprite to the screen.
     *
     * @param d the draw surface
     */
    @Override
    public void drawOn(DrawSurface d) {
        // Display the level on the bar
        d.setColor(TEXT_COLOR);
        d.drawText(getX(), START_Y, "Level name: " + levelName, TEXT_SIZE);
    }
}
