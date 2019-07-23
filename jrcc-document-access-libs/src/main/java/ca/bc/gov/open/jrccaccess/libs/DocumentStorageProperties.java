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
	private String MD5;
	
	/**
	 * Default construtor
	 * @param key - The document key
	 * @param MD5 - The document MD5 in hex format
	 */
	@JsonCreator
	public DocumentStorageProperties(
			@JsonProperty("key")String key,
			@JsonProperty("md5")String MD5) {
		
		if(key == null || key.isEmpty()) throw new IllegalArgumentException("key");
		if(MD5 == null || MD5.isEmpty()) throw new IllegalArgumentException("MD5");
		if(!MD5.matches("-?[0-9a-fA-F]+")) throw new IllegalArgumentException("MD5 must be an hexadecimal string only");
		
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
