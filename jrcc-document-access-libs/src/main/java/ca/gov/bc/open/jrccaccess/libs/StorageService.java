package ca.gov.bc.open.jrccaccess.libs;

import ca.gov.bc.open.jrccaccess.libs.services.ServiceUnavailableException;

/**
 * The StorageService interface provides implementation details for the service.
 * @author alexjoybc
 * @since 0.0.1
 */
public interface StorageService {
	
	/**
	 * Stores a document as a string
	 * @param content Content to be stored
	 * @return The document storage properties
	 */
	DocumentStorageProperties putString(String content) throws ServiceUnavailableException;	

}
