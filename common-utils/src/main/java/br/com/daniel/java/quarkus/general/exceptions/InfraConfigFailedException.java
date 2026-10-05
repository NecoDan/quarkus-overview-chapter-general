package br.com.daniel.java.quarkus.general.exceptions;
public class InfraConfigFailedException extends RuntimeException {

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with
     * <code>null</code> as its detail message.
     */
    public InfraConfigFailedException() {
        super();
    }

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with
     * <code>null</code> as its detail message.
     */
    public InfraConfigFailedException(Exception cause) {
        super(cause);
    }

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public InfraConfigFailedException(String message) {
        super(message);
    }

    /**
     * Constructs a new <code>EntityCreateFailedException</code> exception with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public InfraConfigFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
