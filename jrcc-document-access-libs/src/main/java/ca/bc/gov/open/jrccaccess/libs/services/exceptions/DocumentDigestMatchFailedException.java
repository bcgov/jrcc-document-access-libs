package ca.bc.gov.open.jrccaccess.libs.services.exceptions;

public class DocumentDigestMatchFailedException extends DocumentMessageException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1536183905439920933L;
	
	public DocumentDigestMatchFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentDigestMatchFailedException(String message) {
		super(message);
	}

}
