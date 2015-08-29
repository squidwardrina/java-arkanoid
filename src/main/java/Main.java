import animation.AnimationRunner;
import biuoop.KeyboardSensor;
import io.FormatException;
import run.Arkanoid;
import utils.Finals;

import java.io.IOException;

/**
 * Arkanoid game main class.
 */
public class Main {

    /**
     * Runs the game.
     *
     * @param args level numbers in chosen order
     */
    public static void main(String[] args) {
        AnimationRunner runner = new AnimationRunner(
                Finals.getInstance().getFramesPerSec());
        KeyboardSensor keyboardSensor
                = runner.getGui().getKeyboardSensor();

        // Create the game
        Arkanoid game = new Arkanoid(
                runner, keyboardSensor, getLevelSetFilename(args));

        // Try to run the game
        try {
            game.runArkanoid();

            // Take care of errors which may occur during file processing
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the name of the level sets file. If not specified in args - returns
     * default.
     *
     * @param args the args for the program call
     * @return level sets file name
     */
    private static String getLevelSetFilename(String[] args) {
        // Set specified file if got args. Default file otherwise
        if (args.length > 0) {
            return args[0];
        } else {
            return Finals.getInstance().getDefaultLevelSetsFile();
        }
    }
}
