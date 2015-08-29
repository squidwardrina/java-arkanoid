package io;

import animation.MenuAnimation;
import biuoop.KeyboardSensor;
import gameplay.GameFlow;
import gameplay.LevelInformation;
import menu.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Reads the level sets file.
 */
public class LevelSetsReader {
    private KeyboardSensor keyboardSensor;
    private GameFlow gameFlow;

    /**
     * Creates the level sets reader.
     *
     * @param keyboardSensor the keyboard sensor
     * @param gameFlow       the game flow object
     */
    public LevelSetsReader(KeyboardSensor keyboardSensor, GameFlow gameFlow) {
        this.keyboardSensor = keyboardSensor;
        this.gameFlow = gameFlow;
    }

    /**
     * Reads the level sets file and makes a menu of level sets.
     *
     * @param reader the level sets file reader
     * @return menu of level sets
     * @throws FormatException wrong specification format
     * @throws IOException     problem with the file
     */
    public MenuAnimation<Task<Void>> fromReader(java.io.Reader reader)
            throws FormatException, IOException {
        MenuAnimation<Task<Void>> menu
                = new MenuAnimation<Task<Void>>(keyboardSensor);
        BufferedReader stream = new BufferedReader(reader);

        // Go over level sections and make levels
        String nameLine = stream.readLine();
        while (nameLine != null) {
            // Get the levelset name parameters: 0 - key, 1 - name
            String[] nameParams = splitNameParams(nameLine);
            String key = nameParams[0];
            String message = "Press \"" + key + "\" for " + nameParams[1];

            // Read the description line
            String levelsFilename = getLevelFilename(stream);

            // Add the new entry to the menu
            menu.addSelection(key, message, getLevelSetTask(levelsFilename));
            nameLine = stream.readLine();
        }

        // Finished all the levels. Return the prepared list
        return menu;
    }

    /**
     * Gets level file name from next line in the stream.
     *
     * @param stream stream starting with file name
     * @return the file name
     * @throws IOException     problem reading
     * @throws FormatException wrong format
     */
    private String getLevelFilename(BufferedReader stream)
            throws IOException, FormatException {
        String levelSetFilename = stream.readLine();
        if (levelSetFilename == null) {
            throw new FormatException();
        }
        return levelSetFilename;
    }

    /**
     * Splits name parameters string and checks they are OK.
     *
     * @param nameLine name line as string
     * @return array of params
     * @throws FormatException wrong file format
     */
    private String[] splitNameParams(String nameLine) throws FormatException {
        String[] nameParams = nameLine.split(":");
        if (nameParams.length != 2) {
            throw new FormatException();
        }
        return nameParams;
    }

    /**
     * Creates the task that runs the proper level set.
     *
     * @param levelsFilename the name of the level set file
     * @return the new task that runs the proper level set
     * @throws IOException     problem with the file
     * @throws FormatException wrong file format
     */
    private Task<Void> getLevelSetTask(String levelsFilename)
            throws IOException, FormatException {
        // Crate list of levels out of the proper file
        final List<LevelInformation> levels = getLevels(levelsFilename);

        // Return the new task running the levels
        return new Task<Void>() {
            public Void run() {
                gameFlow.runLevels(levels);
                return null;
            }
        };
    }

    /**
     * Gets a list of levels to run out of the file.
     *
     * @param levelsFilename the name of the levels file
     * @return the list of levels
     * @throws IOException problem with file
     * @throws FormatException wrong format
     */
    private List<LevelInformation> getLevels(String levelsFilename)
            throws IOException, FormatException {
        InputStreamReader stream = null;
        List<LevelInformation> levels = null;

        // Try to open file and read the levels
        try {
            // Open stream
            InputStream is = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(levelsFilename);
            if (is == null) {
                throw new IOException();
            }
            Charset utf8 = Charset.forName("UTF-8");
            stream = new InputStreamReader(is, utf8);

            // Read levels
            LevelSpecificationReader levelReader
                    = new LevelSpecificationReader();
            levels = levelReader.fromReader(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return levels;
    }
}
