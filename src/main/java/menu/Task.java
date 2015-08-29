package menu;

/**
 * Represents one task in the menu.
 *
 * @param <T> value to be returned after executing the task
 */
public interface Task<T> {
    /**
     * Executes the task.
     *
     * @return the return value of the task (may be Void)
     */
    T run();
}