package ca.gov.bc.open.jrccaccess.libs;

import java.text.MessageFormat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represent information about the document
 * @author alexjoybc
 * @since 0.1.0
 */
public class DocumentInfo {

	/**
	 * Represents the type of document beeing processed
	 */
	private String type;
	
	@JsonCreator
	public DocumentInfo(@JsonProperty("type")String type) {
		if(type == null || type.isEmpty()) throw new IllegalArgumentException("type");
		this.type = type;
	}
	
	
	public String getType() {
		return type;
	}
	
	
	@Override
	public String toString()
	{
		return MessageFormat.format("document type: {0}", this.getType());
	}
}
