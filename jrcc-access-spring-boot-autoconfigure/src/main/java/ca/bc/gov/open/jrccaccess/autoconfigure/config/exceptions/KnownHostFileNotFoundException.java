package ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions;

/**
 * An exception which is thrown when application cannot find specified known_hosts file.
 */
public class KnownHostFileNotFoundException extends InvalidConfigException{
    /**
     * Constructs a new InvalidConfigException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public KnownHostFileNotFoundException(String message) {
        super(message);
    }
}
