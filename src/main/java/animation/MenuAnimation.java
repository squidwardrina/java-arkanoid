package animation;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import menu.Menu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The user menu animation for the arkanoid game.
 *
 * @param <T>
 */
public class MenuAnimation<T> implements Animation, Menu<T> {
    private T status = null;
    private Map<String, T> menuOptions = new TreeMap<String, T>();
    private List<String> messages = new ArrayList<String>();
    private KeyboardSensor keyboardSensor;
    private AnimationRunner runner;
    private boolean stop = false;
    private Map<String, Menu<T>> subMenus = new TreeMap<String, Menu<T>>();

    /**
     * Creates a "leaf" menu: cannot run any sub-menu.
     *
     * @param keyboardSensor the key sensor
     */
    public MenuAnimation(KeyboardSensor keyboardSensor) {
        this.keyboardSensor = keyboardSensor;
        this.runner = null;
    }

    /**
     * Creates the menu which can run a sub menu from it.
     *
     * @param sensor the key sensor
     * @param runner animation runner
     */
    public MenuAnimation(KeyboardSensor sensor, AnimationRunner runner) {
        this.keyboardSensor = sensor;
        this.runner = runner;
    }

    /**
     * Adds a select option to the menu.
     *
     * @param key       key to be pressed
     * @param message   message to print on screen
     * @param returnVal the return value of the option
     */
    public void addSelection(String key, String message, T returnVal) {
        this.menuOptions.put(key, returnVal);
        this.messages.add(message);
    }

    /**
     * Adds sub menu to the current menu.
     *
     * @param key     key to be pressed to enter the sub menu
     * @param message message to print on the screen
     * @param subMenu the new sub menu
     */
    public void addSubMenu(String key, String message, Menu<T> subMenu) {
        subMenus.put(key, subMenu);
        messages.add(message);
    }

    /**
     * Returns the menu status (the chosen option).
     *
     * @return the chosen menu option
     */
    public T getStatus() {
        return status;
    }

    /**
     * One animation step.
     *
     * @param d  is the surface
     * @param dt is the time of the move
     */
    public void doOneFrame(DrawSurface d, double dt) {
        drawBackground(d);
        displayChoices(d);
        checkUserAction();
    }

    /**
     * Indicates whether the animation should stop or not.
     *
     * @return true / false
     */
    public boolean shouldStop() {
        boolean isToStop = this.stop;    // check if needs to stop
        this.stop = false;    // update the flag for future run
        return isToStop;
    }

    /**
     * Draws the menu background.
     *
     * @param d draw surface
     */
    private void drawBackground(DrawSurface d) {
        // Background color
        d.setColor(new Color(204, 255, 238));
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());

        // Size and color of the text
        d.setColor(new Color(25, 204, 181));
        d.drawText(20, 100, "Arkanoid", 60);


        // Drawing the sun
        int xLineEnd = 0;
        int dx = d.getHeight() / 50;

        d.setColor(new Color(255, 231, 51));
        for (int i = 0; i < 100; i++) {
            xLineEnd += dx;
            d.drawLine(d.getWidth(), 0, xLineEnd, d.getHeight());
        }

        d.setColor(new Color(255, 231, 51));
        d.fillCircle(800, 40, 90);

        // Drawing the rainbow
        d.setColor(new Color(112, 51, 178));
        d.fillCircle(400, 600, 300);
        d.setColor(new Color(182, 102, 229));
        d.fillCircle(400, 600, 280);
        d.setColor(new Color(229, 25, 150));
        d.fillCircle(400, 600, 260);
        d.setColor(new Color(229, 76, 215));
        d.fillCircle(400, 600, 240);
        d.setColor(new Color(255, 146, 102));
        d.fillCircle(400, 600, 220);
        d.setColor(new Color(255, 149, 0));
        d.fillCircle(400, 600, 200);
        d.setColor(new Color(255, 237, 102));
        d.fillCircle(400, 600, 180);
        d.setColor(new Color(167, 255, 76));
        d.fillCircle(400, 600, 160);
        d.setColor(new Color(63, 204, 25));
        d.fillCircle(400, 600, 140);
        d.setColor(new Color(76, 255, 145));
        d.fillCircle(400, 600, 120);
        d.setColor(new Color(127, 218, 255));
        d.fillCircle(400, 600, 100);
        d.setColor(new Color(51, 141, 255));
        d.fillCircle(400, 600, 80);
        d.setColor(new Color(51, 65, 178));
        d.fillCircle(400, 600, 60);
        d.setColor(new Color(0, 8, 76));
        d.fillCircle(400, 600, 40);
    }

    /**
     * Displays all the menu options.
     *
     * @param d draw surface
     */
    private void displayChoices(DrawSurface d) {
        d.setColor(Color.BLACK);
        int height = 200;

        // Go over the messages and draw them
        for (String message : this.messages) {
            d.drawText(20, height, message, 20);
            height += 30;
        }
    }

    /**
     * If user chose an option - update status and stop.
     */
    private void checkUserAction() {
        // Go over menu options and return if needed
        for (String key : menuOptions.keySet()) {
            // Check if user chose this option
            if (keyboardSensor.isPressed(key)) {
                status = menuOptions.get(key);
                stop = true;
                break;
            }
        }

        // Go over sub menus and run if needed
        for (final String key : subMenus.keySet()) {
            // Check if user chose this option
            if (keyboardSensor.isPressed(key)) {
                status = runSubMenu(subMenus.get(key));
                stop = true;
                break;
            }
        }
    }

    /**
     * Runs the new sub-menu of this menu.
     *
     * @param subMenu the new sub-menu tu run
     * @return the status of the sub-menu
     */
    private T runSubMenu(Menu<T> subMenu) {
        runner.run(subMenu); // run the menu
        return subMenu.getStatus(); // wait for user selection
    }
}

