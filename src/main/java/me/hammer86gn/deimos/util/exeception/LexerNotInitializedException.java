package me.hammer86gn.deimos.util.exeception;

/**
 * Thrown when you try to use the lexer before initializing it
 */
public class LexerNotInitializedException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public LexerNotInitializedException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public LexerNotInitializedException(String message) {
        super(message);
    }
}
