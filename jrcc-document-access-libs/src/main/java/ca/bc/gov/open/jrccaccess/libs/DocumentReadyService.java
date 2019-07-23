package ca.bc.gov.open.jrccaccess.libs;

import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;

/**
 * Represents a service to manipulate document ready message
 * 
 * @author 177226
 *
 */
public interface DocumentReadyService {

	/**
	 * publish a document ready message to the document ready topic with a
	 * predefined routing key.
	 * 
	 * @param message
	 */
	public void publish(DocumentReadyMessage message)  throws ServiceUnavailableException;

}
