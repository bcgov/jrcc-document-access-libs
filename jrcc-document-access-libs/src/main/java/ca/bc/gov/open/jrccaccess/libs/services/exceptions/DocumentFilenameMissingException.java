package ca.bc.gov.open.jrccaccess.libs.services.exceptions;

/**
 * The DocumentFilenameMissingException indicates that the document does not have filename.
 */
public class DocumentFilenameMissingException extends DocumentMessageException{
    /**
     * Constructs a DocumentFilenameMissingException with the specified exception
     * message details.
     * @param message	the detail message. The detail message is saved
     * 			for later retrieval by the {@link #getMessage()} method.
     */
    public DocumentFilenameMissingException(String message) { super(message); }


    /**
     *  Constructs a DocumentFilenameMissingException with the specified exception
     *  message details and the inner cause
     * @param message	the detail message. The detail message is saved
     * 			for later retrieval by the {@link #getMessage()} method.
     * @param cause	the cause (which is saved for later retrieval by the
     *         	{@link #getCause()} method).  (A <tt>null</tt> value is
     *         	permitted, and indicates that the cause is nonexistent or
     *         	unknown.)
     */
    public DocumentFilenameMissingException(String message, Throwable cause) { super(message, cause); }
}
