package ca.bc.gov.open.jrccaccess.libs.services.exceptions;

/**
 * The {@link RuntimeException} extension to indicate that a service (server) 
 * 		is temporary not available.
 * @author alexjoybc
 * @since 0.1.0
 */
public class ServiceUnavailableException extends DocumentMessageException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new service unavailable exception with the specified 
	 * detail message.
	 * @param message	the detail message. The detail message is saved
	 * 			for later retrieval by the {@link #getMessage()} method.
	 */
	public ServiceUnavailableException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new service unavailable exception with the specified 
	 * detail message and cause.
	 * @param message	the detail message. The detail message is saved 
 * 				for later retrieval by the {@link #getMessage()} method.
	 * @param cause	the cause (which is saved for later retrieval by the
     *         	{@link #getCause()} method).  (A <tt>null</tt> value is
     *         	permitted, and indicates that the cause is nonexistent or
     *         	unknown.)
	 */
	public ServiceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}
