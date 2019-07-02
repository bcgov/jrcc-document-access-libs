package ca.gov.bc.open.jrccaccess.libs;

public interface StorageService {
	
	/**
	 * Stores a document as a string
	 * @param content Content to be stored
	 * @return The document storage properties
	 */
	DocumentStorageProperties putString(String content);	

}
