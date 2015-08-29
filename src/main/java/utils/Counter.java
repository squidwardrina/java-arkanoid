package utils;

/**
 * A simple class used for counting things.
 */
public class Counter {
    private int count;

    /**
     * Constructs a new counter.
     *
     * @param number the count to start at
     */
    public Counter(int number) {
        this.count = number;
    }

    /**
     * Add number to current count.
     *
     * @param number number to add
     */
    public void increase(int number) {
        this.count += number;
    }

    /**
     * Subtract number from current count.
     *
     * @param number number to subtract
     */
    public void decrease(int number) {
        this.count -= number;
    }

    /**
     * Get current count.
     *
     * @return current count
     */
    public int getValue() {
        return this.count;
    }
}