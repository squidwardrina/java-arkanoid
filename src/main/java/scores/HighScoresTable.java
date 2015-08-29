package scores;

import utils.Finals;

import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a table of high scores - player name + score.
 */
public class HighScoresTable {
    private int size;
    private List<ScoreInfo> scores;

    /**
     * Create an empty high-scores table with the specified size.
     *
     * @param size number of top scores to hold
     */
    public HighScoresTable(int size) {
        this.size = size;
        scores = new ArrayList<ScoreInfo>();
    }

    /**
     * Read a table from file and return it.
     *
     * @param filename is the file to read from
     * @return the scores table
     */
    public static HighScoresTable loadFromFile(File filename) {
        HighScoresTable scoresTable = null;
        BufferedReader inputStream = null;
        Charset utf8 = Charset.forName("UTF-8");

        // Try to open the file and read from it
        try {
            inputStream = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filename), utf8));

            // Create a new table
            scoresTable = new HighScoresTable(
                    Finals.getInstance().getScoresToKeep());

            // Read a line of score info and split it to a new entry
            String scoreInfoStr;
            while ((scoreInfoStr = inputStream.readLine()) != null) {
                String[] params = scoreInfoStr.split(
                        Finals.getInstance().getScoreDelimiter());
                scoresTable.add(new ScoreInfo(params[0],
                        Integer.decode(params[1])));
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while reading!");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Failed closing the file!");
                }
            }
        }

        // Return the score table
        return scoresTable;
    }

    /**
     * Add a high-score if it fits.
     *
     * @param score score to add
     */
    public void add(ScoreInfo score) {
        if (this.isToAdd(score.getScore())) {
            this.scores.add(getRank(score.getScore()) - 1, score);
        }
        if (this.scores.size() > this.size) {
            this.scores.remove(this.size);
        }
    }

    /**
     * Returns the place number of a score in the table.
     *
     * @param score the score to be checked
     * @return proper place
     */
    public int getRank(int score) {
        // Go over the scores and find proper place
        int place = 1;
        for (ScoreInfo scoreInfo : scores) {
            if (score < scoreInfo.getScore()) {
                place++;
            } else {
                return place;
            }
        }

        // Return the last place
        return this.scores.size() + 1;
    }

    /**
     * Tell whether the score should be added to the table.
     *
     * @param score score of the player to check
     * @return check result
     */
    public boolean isToAdd(int score) {
        List<ScoreInfo> highScores = this.getHighScores();

        // If table is not full - add
        if (highScores.size() < this.size()) {
            return true;
        }

        // Add if big enough
        return highScores.get(highScores.size() - 1).getScore() <= score;
    }

    /**
     * Return table size.
     *
     * @return size of the table
     */
    public int size() {
        return this.size;
    }

    /**
     * Return a sorted list of high scores.
     *
     * @return sorted list of high scores.
     */
    public List<ScoreInfo> getHighScores() {
        return this.scores;
    }

    /**
     * Load table data from file.
     *
     * @param filename the file to read from
     * @throws IOException any exception with the input
     */
    public void load(File filename) throws IOException {
        this.clear();
        Charset utf8 = Charset.forName("UTF-8");
        BufferedReader inputStream = null;

        // Try to open the file and read from it
        try {
            inputStream = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filename), utf8));

            // Read a line of score info and split it to a new entry
            String scoreInfoStr;
            while ((scoreInfoStr = inputStream.readLine()) != null) {
                String[] params = scoreInfoStr.split(
                        Finals.getInstance().getScoreDelimiter());
                this.add(new ScoreInfo(params[0],
                        Integer.decode(params[1])));
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while reading!");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Failed closing the file!");
                }
            }
        }
    }

    /**
     * Clears the table.
     */
    public void clear() {
        this.scores.clear();
    }

    /**
     * Save table data to the specified file.
     *
     * @param filename file to save to
     * @throws IOException any IO exception
     */
    public void save(File filename) throws IOException {
        Charset utf8 = Charset.forName("UTF-8");
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(filename), utf8));

            // Go over the scores and save to file
            for (ScoreInfo scoreInfo : scores) {
                outputStream.printf("%s%s%d\n", scoreInfo.getName(),
                        Finals.getInstance().getScoreDelimiter(),
                        scoreInfo.getScore());
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while writing!");
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}