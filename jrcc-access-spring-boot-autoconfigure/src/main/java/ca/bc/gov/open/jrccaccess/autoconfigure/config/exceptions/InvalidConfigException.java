package ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions;

/**
 * A base application exception indicating something is wrong in application setting file(such as application.settings or application.yml).
 */
public class InvalidConfigException extends Exception{

    /**
     * Constructs a new InvalidConfigException with the specified detail message.
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public InvalidConfigException(String message) {
        super(message);
    }
}
