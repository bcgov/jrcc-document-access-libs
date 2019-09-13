package ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions;

/**
 * An exception which is thrown when using sftp known_hosts file is not defined in configuration while allowUnknownKey is set to false.
 *
 */
public class KnownHostFileNotDefinedException extends InvalidConfigException {
    /**
     * Constructs a new KnownHostFileNotDefinedException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public KnownHostFileNotDefinedException(String message) {
        super(message);
    }
}
