package menu;

import animation.Animation;

/**
 * Represents a user menu.
 *
 * @param <T> type of select options in the menu
 */
public interface Menu<T> extends Animation {
    /**
     * Adds a select option to the menu.
     *
     * @param key key to be pressed
     * @param message message to print on screen
     * @param returnVal the return value of the option
     */
    void addSelection(String key, String message, T returnVal);

    /**
     * Adds sub menu to the current menu.
     *
     * @param key key to be pressed to enter the sub menu
     * @param message message to print on the screen
     * @param subMenu the new sub menu
     */
    void addSubMenu(String key, String message, Menu<T> subMenu);

    /**
     * Returns the menu status (the chosen option).
     *
     * @return the chosen menu option
     */
    T getStatus();


}