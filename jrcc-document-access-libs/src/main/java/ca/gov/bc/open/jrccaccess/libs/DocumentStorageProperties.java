package ca.gov.bc.open.jrccaccess.libs;

/**
 * Represents the document storage properties
 * @author 177226
 *
 */
public class DocumentStorageProperties {
	
	/**
	 * The key of the document
	 */
	private String key;	
		
	/**
	 * The MD5 of the document in hex format
	 */
	private String MD5;
	
	/**
	 * Default construtor
	 * @param key - The document key
	 * @param MD5 - The document MD5 in hex format
	 */
	public DocumentStorageProperties(
			String key,
			String MD5) {
		
		if(key == null || key.isEmpty()) throw new IllegalArgumentException("key");
		if(MD5 == null || MD5.isEmpty()) throw new IllegalArgumentException("MD5");
		
		this.key = key;
		this.MD5 = MD5;
	}
	
	public String getKey() {
		return key;
	}

	public String getMD5() {
		return MD5;
	}

}
