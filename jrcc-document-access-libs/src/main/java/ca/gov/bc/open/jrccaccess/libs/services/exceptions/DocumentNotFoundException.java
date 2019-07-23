package ca.gov.bc.open.jrccaccess.libs.services.exceptions;

/**
 * The documentNotFoundException indicates that the document is not available for processing.
 */
public class DocumentNotFoundException extends DocumentMessageException {

    /**
     * Constructs a DocumentNotFoundException with the specified exception
     * message details.
     * @param message	the detail message. The detail message is saved
     * 			for later retrieval by the {@link #getMessage()} method.
     */
    public DocumentNotFoundException(String message) { super(message); }


    /**
     *  Constructs a DocumentNotFoundException with the specified exception
     *  message details and the inner cause
     * @param message	the detail message. The detail message is saved
     * 			for later retrieval by the {@link #getMessage()} method.
     * @param cause	the cause (which is saved for later retrieval by the
     *         	{@link #getCause()} method).  (A <tt>null</tt> value is
     *         	permitted, and indicates that the cause is nonexistent or
     *         	unknown.)
     */
    public DocumentNotFoundException(String message, Throwable cause) { super(message, cause); }

}
