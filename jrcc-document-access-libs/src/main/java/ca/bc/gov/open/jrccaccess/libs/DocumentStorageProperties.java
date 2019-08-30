package ca.bc.gov.open.jrccaccess.libs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
	private String digest;
	
	/**
	 * Default construtor
	 * @param key - The document key
	 * @param md5 - The document MD5 in hex format
	 */
	@JsonCreator
	public DocumentStorageProperties(
			@JsonProperty("key")String key,
			@JsonProperty("digest")String digest) {
		
		if(key == null || key.isEmpty()) throw new IllegalArgumentException("key");
		if(digest == null || digest.isEmpty()) throw new IllegalArgumentException("digest");
		if(!digest.matches("-?[0-9a-fA-F]+")) throw new IllegalArgumentException("Digest must be an hexadecimal string only");
		
		this.key = key;
		this.digest = digest;
	}
	
	public String getKey() {
		return key;
	}

	public String getDigest() {
		return this.digest;
	}

}
