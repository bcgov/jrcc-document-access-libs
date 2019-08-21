package ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions;

public class OutputConfigMissingException extends InvalidConfigException {

    public OutputConfigMissingException() {
        super("Missing bcgov.access.output part in application settings.");
    }

}
