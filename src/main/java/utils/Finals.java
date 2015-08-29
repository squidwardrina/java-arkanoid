package utils;

/**
 * Holds all the finals needed for the game.
 */
public final class Finals {
    private static Finals instance;
    private final int framesPerSec = 60;
    private final int gameHeight = 600;
    private final int gameWidth = 800;
    private final int margin = 5;
    private final double paddleHeight = 20;
    private final double paddleYStart = gameHeight - paddleHeight - margin;
    private final int statusBarHeight = 20;
    private final int scoresToKeep = 5;
    private final String scoreDelimiter = "_:_";
    private final String scoresFileName = "highscores";
    private final String stopAnimationKey = "space";
    private final String defaultLevelSets = "level_sets.txt";
    private final int lives = 7;

    /**
     * Creates the instance.
     */
    private Finals() {
    }

    /**
     * Returns the finals class instance.
     *
     * @return instance of finals class
     */
    public static Finals getInstance() {
        if (Finals.instance == null) {
            Finals.instance = new Finals();
        }
        return instance;

    }

    /**
     * Returns number of scores to keep in top scores table.
     *
     * @return number of scores to keep.
     */
    public int getScoresToKeep() {
        return scoresToKeep;
    }

    /**
     * Returns the name of the scores file.
     *
     * @return the name of the scores file
     */
    public String getScoresFileName() {
        return scoresFileName;
    }

    /**
     * Returns the delimiter between name & score.
     *
     * @return the delimiter between name & score
     */
    public String getScoreDelimiter() {
        return scoreDelimiter;
    }

    /**
     * Get game height.
     *
     * @return game screen height
     */
    public int getGameHeight() {
        return gameHeight;
    }

    /**
     * Gets game frame time.
     *
     * @return one frame time
     */
    public int getFramesPerSec() {
        return framesPerSec;
    }

    /**
     * Gets game screen width.
     *
     * @return game screen width
     */
    public int getGameWidth() {
        return gameWidth;
    }

    /**
     * Gets game screen margin.
     *
     * @return gets game margin
     */
    public int getMargin() {
        return margin;
    }

    /**
     * Gets paddle height.
     *
     * @return paddle height
     */
    public double getPaddleHeight() {
        return paddleHeight;
    }

    /**
     * Gets paddle's start point at Y axe.
     *
     * @return paddle's y start
     */
    public double getPaddleYStart() {
        return paddleYStart;
    }

    /**
     * Gets the height of the status bar.
     *
     * @return status bar height
     */
    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    /**
     * Gets the key that would stop animation.
     *
     * @return the key that would stop animation
     */
    public String getStopAnimationKey() {
        return stopAnimationKey;
    }

    /**
     * Gets lives number for the game.
     *
     * @return number of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returnes the default level sets file name.
     *
     * @return  the default level sets file name.
     */
    public String getDefaultLevelSetsFile() {
        return defaultLevelSets;
    }
}
