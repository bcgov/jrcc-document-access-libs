package ca.gov.bc.open.jrccaccess.libs;

import ca.gov.bc.open.jrccaccess.libs.services.ServiceUnavailableException;

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
	public void Publish(DocumentReadyMessage message)  throws ServiceUnavailableException;

}
