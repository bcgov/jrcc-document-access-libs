package ca.bc.gov.open.jrccaccess.autoconfigure;

/**
 * A class define some common variables .
 */
public class Common {

    /**
     * Represents Java Logging MDC KEY - filename
     */
    public static final String MDC_KEY_FILENAME="transaction.filename";

    private Common() {
        throw new IllegalStateException("Common class");
    }
}
