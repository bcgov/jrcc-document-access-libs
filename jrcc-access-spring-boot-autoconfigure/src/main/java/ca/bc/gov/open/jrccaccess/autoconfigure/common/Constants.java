package ca.bc.gov.open.jrccaccess.autoconfigure.common;

/**
 * A class define some common Constants .
 */
public class Constants {

    /**
     * Represents Java Logging MDC KEY - filename
     */
    public static final String MDC_KEY_FILENAME="transaction.filename";

    private Constants() {
        throw new IllegalStateException("Constants class");
    }
}
