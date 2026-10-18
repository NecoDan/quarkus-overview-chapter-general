package br.com.daniel.java.quarkus.general.exceptions;

public class ParseEntityFailedException extends RuntimeException {

    /**
     * Constructs a new <code>{@link ParseEntityFailedException}</code> exception with
     * <code>null</code> as its detail message.
     */
    public ParseEntityFailedException() {
        super();
    }

    /**
     * Constructs a new <code>{@link ParseEntityFailedException}</code> exception with
     * <code>null</code> as its detail message.
     */
    public ParseEntityFailedException(Exception cause) {
        super(cause);
    }

    /**
     * Constructs a new <code>{@link ParseEntityFailedException}</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public ParseEntityFailedException(String message) {
        super(message);
    }

    /**
     * Constructs a new <code>{@link ParseEntityFailedException}</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public ParseEntityFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
