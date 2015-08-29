package run;

import animation.AnimationRunner;
import animation.HighScoresAnimation;
import animation.KeyPressStoppableAnimation;
import animation.MenuAnimation;
import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import gameplay.GameFlow;
import io.FormatException;
import io.LevelSetsReader;
import menu.Task;
import scores.HighScoresTable;
import utils.Finals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * This class can run the arkanoid game.
 */
public class Arkanoid {
    private Finals finals = Finals.getInstance();
    private AnimationRunner runner;
    private KeyboardSensor sensor;
    private String levelSetsFilename;

    /**
     * Creates the arkanoid game session.
     *
     * @param runner            the animation runner for the game session
     * @param sensor            the keyboard sensor for user unput
     * @param levelSetsFilename name of the level sets file
     */
    public Arkanoid(AnimationRunner runner, KeyboardSensor sensor,
                    String levelSetsFilename) {
        this.runner = runner;
        this.sensor = sensor;
        if (levelSetsFilename != null) {
            this.levelSetsFilename = levelSetsFilename;
        } else {
            // Level sets file not specified. Get the default
            this.levelSetsFilename
                    = Finals.getInstance().getDefaultLevelSetsFile();
        }
    }

    /**
     * Runs the game menu.
     *
     * @throws IOException     problem with file
     * @throws FormatException wrong format
     */
    public void runArkanoid() throws IOException, FormatException {
        showMainMenu();
    }

    /**
     * Displays the user menu and prepares the chosen tasks.
     *
     * @throws IOException        problem with file
     * @throws io.FormatException format problem
     */
    private void showMainMenu() throws IOException, FormatException {
        // Run the menu until user exits
        while (true) {
            // Creating new menu each time to refresh the levels
            MenuAnimation<Task<Void>> menu = createMenu();
            runner.run(menu); // run the menu
            Task<Void> task = menu.getStatus(); // wait for user selection
            task.run();
        }
    }

    /**
     * Creates the game menu with relevant options.
     *
     * @return the new created menu
     * @throws IOException        problem with file
     * @throws io.FormatException format problem
     */
    private MenuAnimation<Task<Void>> createMenu()
            throws IOException, FormatException {
        MenuAnimation<Task<Void>> menu
                = new MenuAnimation<Task<Void>>(sensor, runner);
        MenuAnimation<Task<Void>> levelSetsMenu = createLevelSetsMenu();

        // Add menu options
        menu.addSubMenu("s", "Press \"s\" to start a new game.",
                levelSetsMenu);
        menu.addSelection("h", "Press \"h\" to see the high scores.",
                scoresOption());
        menu.addSelection("q", "Press \"q\" to quit.", quitOption());

        return menu; // return the prepared menu
    }

    /**
     * Creates the quit task.
     *
     * @return quit task
     */
    private Task<Void> quitOption() {
        return new Task<Void>() {
            // Quits the game
            public Void run() {
                System.exit(0);
                return null;
            }
        };
    }

    /**
     * Creates the showing high scores task.
     *
     * @return showing high scores task
     */
    private Task<Void> scoresOption() {
        // Get the high score table file
        File scoresFile = new File(finals.getScoresFileName());

        // If the file exists - return task displaying the table
        if (scoresFile.exists()) {
            final HighScoresTable scoresTable
                    = HighScoresTable.loadFromFile(scoresFile);

            // Create the screen task
            return new Task<Void>() {
                // Create the screen animation
                private KeyPressStoppableAnimation scoresScreen
                        = new KeyPressStoppableAnimation(sensor,
                        new HighScoresAnimation(scoresTable),
                        finals.getStopAnimationKey());

                // Runs the animation
                public Void run() {
                    runner.run(scoresScreen);
                    return null;
                }
            };
        } else { // no file to show. Only display message
            return new Task<Void>() {
                // Create the screen animation
                private KeyPressStoppableAnimation scoresScreen
                        = new KeyPressStoppableAnimation(sensor,
                        new HighScoresAnimation(null),
                        finals.getStopAnimationKey());

                // Runs the animation
                public Void run() {
                    runner.run(scoresScreen);
                    return null;
                }
            };
        }
    }

    /**
     * Creates the level sets sub-menu for the user.
     *
     * @return new menu
     * @throws IOException        problem with file
     * @throws io.FormatException problem with file
     */
    private MenuAnimation<Task<Void>> createLevelSetsMenu()
            throws IOException, FormatException {
        Charset utf8 = Charset.forName("UTF-8");
        InputStreamReader stream = null;
        MenuAnimation<Task<Void>> setsMenu = null;

        // Try to open the level sets file and read from it
        try {
            InputStream is = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(levelSetsFilename);
            if (is == null) {
                throw new IOException();
            }
            stream = new InputStreamReader(is, utf8);

            // Create the menu from file
            int lives = Finals.getInstance().getLives();
            GameFlow gameFlow = new GameFlow(runner, sensor, lives);
            setsMenu = new LevelSetsReader(sensor, gameFlow).fromReader(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return setsMenu;
    }
}