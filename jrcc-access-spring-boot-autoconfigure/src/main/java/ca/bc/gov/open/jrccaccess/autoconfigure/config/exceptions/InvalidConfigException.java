package ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions;

public class InvalidConfigException extends Exception{
    public InvalidConfigException() {
        super();
    }

    public InvalidConfigException(String message) {
        super(message);
    }
}
