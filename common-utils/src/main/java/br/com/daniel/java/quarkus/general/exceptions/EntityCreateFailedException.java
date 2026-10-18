package br.com.daniel.java.quarkus.general.exceptions;
public class EntityCreateFailedException extends RuntimeException {

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with
     * <code>null</code> as its detail message.
     */
    public EntityCreateFailedException() {
        super();
    }

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with
     * <code>null</code> as its detail message.
     */
    public EntityCreateFailedException(Exception cause) {
        super(cause);
    }

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public EntityCreateFailedException(String message) {
        super(message);
    }

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public EntityCreateFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
