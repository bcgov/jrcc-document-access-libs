package ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions;

public class InputConfigMissingException extends InvalidConfigException{
    public InputConfigMissingException() {
        super("Missing bcgov.access.input part in application settings.");
    }
}
