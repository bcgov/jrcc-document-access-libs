package ca.gov.bc.open.jrccaccess.libs;

import ca.gov.bc.open.jrccaccess.libs.services.exceptions.DocumentMessageException;

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
	DocumentStorageProperties putString(String content) throws DocumentMessageException;	
	
	/**
	 * Gets a document from storage as a string
	 * @param key object key to retrieve from storage
	 * @param digest MD5 Digest
	 * @return Content from storage
	 */
	public String getString(String key, String digest) throws DocumentMessageException;


}
