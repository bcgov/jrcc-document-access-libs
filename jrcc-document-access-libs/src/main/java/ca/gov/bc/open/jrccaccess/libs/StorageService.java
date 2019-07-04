package ca.gov.bc.open.jrccaccess.libs;

import ca.gov.bc.open.jrccaccess.libs.services.ServiceUnavailableException;

public interface StorageService {
	
	/**
	 * Stores a document as a string
	 * @param content Content to be stored
	 * @return The document storage properties
	 */
	DocumentStorageProperties putString(String content) throws ServiceUnavailableException;	

}
