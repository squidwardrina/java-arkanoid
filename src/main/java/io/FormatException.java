package io;

/**
 * Exception that may occur during the file reading, when encountering wrong
 * format.
 */
public class FormatException extends Exception {
    /**
     * Creates formay exception.
     *
     * @param message exception message
     */
    public FormatException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public FormatException() {
        super("Can't parse information - wrong file format ");
    }
}