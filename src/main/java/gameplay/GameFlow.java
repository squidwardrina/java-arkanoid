package gameplay;

import animation.AnimationRunner;
import animation.GameLevel;
import animation.Animation;
import animation.WinScreen;
import animation.GameOverScreen;
import animation.HighScoresAnimation;
import animation.KeyPressStoppableAnimation;
import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import scores.HighScoresTable;
import scores.ScoreInfo;
import utils.Counter;
import utils.Finals;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Class in charge of creating the game levels and running them one after
 * another.
 */
public class GameFlow {
    private KeyboardSensor keyboardSensor;
    private AnimationRunner animationRunner;
    private HighScoresTable scoresTable;
    private Counter playerScore;
    private Counter livesLeft;

    /**
     * Creates the game flow.
     *
     * @param runner    the animation runner
     * @param keySensor the keyboard sensor
     * @param lives     amount of lives user will have
     */
    public GameFlow(AnimationRunner runner, KeyboardSensor keySensor,
                    int lives) {
        this.animationRunner = runner;
        this.keyboardSensor = keySensor;
        this.playerScore = new Counter(0);
        this.livesLeft = new Counter(lives);

        // Load the high scores table
        this.scoresTable
                = new HighScoresTable(Finals.getInstance().getScoresToKeep());
        loadScores();
    }

    /**
     * Run the game with the list of levels chosen.
     *
     * @param levels list of levels information in running order
     */
    public void runLevels(List<LevelInformation> levels) {
        boolean playerWon = true;

        // Run levels as they are in list
        for (LevelInformation levelInfo : levels) {
            // Create the current level
            GameLevel level = new GameLevel(levelInfo, keyboardSensor,
                    animationRunner, playerScore, livesLeft);
            level.initialize();

            // Play current level while there are more blocks and lives
            while (level.areBlocksLeft() && this.areLivesLeft()) {
                level.playOneTurn();
            }

            // No more lives - stop the game
            if (this.livesLeft.getValue() == 0) {
                playerWon = false;
                break;
            }
        }

        // Game ended
        finishGame(playerWon);
    }

    /**
     * Load the scores from file to the table.
     */
    private void loadScores() {
        File scoresFile = new File(Finals.getInstance().getScoresFileName());
        if (scoresFile.exists()) {
            try {
                this.scoresTable.load(scoresFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save high scores to file.
     */
    private void saveScores() {
        File scoresFile = new File(Finals.getInstance().getScoresFileName());
        try {
            this.scoresTable.save(scoresFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display the game end process.
     *
     * @param playerWon indication whether the player won
     */
    private void finishGame(boolean playerWon) {
        // Display the end screen - win or lose
        displayEndScreen(playerWon);

        // Add the player's score if needed, and display the scores table
        if (this.scoresTable.isToAdd(this.playerScore.getValue())) {
            addHighScore();
        }
        displayHighScores();
    }

    /**
     * Displays the end game screen - "you win" or "you lose".
     *
     * @param playerWon indication whether the player won
     */
    private void displayEndScreen(boolean playerWon) {
        Animation endScreen;
        Finals finals = Finals.getInstance();
        int currScore = this.playerScore.getValue();

        // Decide which screen we need
        if (playerWon) {
            endScreen = new WinScreen(currScore);
        } else {
            endScreen = new GameOverScreen(currScore);
        }

        // Wrap the screen with a stoppable animation
        KeyPressStoppableAnimation stoppableEndScreen =
                new KeyPressStoppableAnimation(this.keyboardSensor,
                endScreen, finals.getStopAnimationKey());
        this.animationRunner.run(stoppableEndScreen);
    }

    /**
     * Displays the high scores screen.
     */
    private void displayHighScores() {
        // Create the screen
        KeyPressStoppableAnimation scoresScreen = new
                KeyPressStoppableAnimation(this.keyboardSensor,
                new HighScoresAnimation(this.scoresTable),
                Finals.getInstance().getStopAnimationKey());

        // Run the screen animation
        this.animationRunner.run(scoresScreen);
    }

    /**
     * Adds new high score and saves updated table to file.
     */
    private void addHighScore() {
        // Create new score info
        int currScore = this.playerScore.getValue();
        String playerName = getPlayerName();
        ScoreInfo scoreInfo = new ScoreInfo(playerName, currScore);

        // Add and save
        scoresTable.add(scoreInfo);
        saveScores();
    }

    /**
     * Gets a name from the user.
     *
     * @return player's name
     */
    private String getPlayerName() {
        DialogManager dialog
                = this.animationRunner.getGui().getDialogManager();
        String name = dialog.showQuestionDialog("New high score!",
                "What is your name?", "");

        // Make sure name doesn't contain the string delimiter and return it
        String scoreDelimiter = Finals.getInstance().getScoreDelimiter();
        return name.replace(scoreDelimiter, "");
    }

    /**
     * Returns whether player has more lives.
     *
     * @return true or false
     */
    private boolean areLivesLeft() {
        return this.livesLeft.getValue() > 0;
    }
}