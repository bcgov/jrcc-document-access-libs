package ca.gov.bc.open.jrccaccess.libs;

/**
 * Represenst information about the document
 * @author alexjoybc
 *
 */
public class DocumentInfo {

	/**
	 * Represents the type of document beeing processed
	 */
	private String type;

	public DocumentInfo(String type) {
		if(type == null || type.isEmpty()) throw new IllegalArgumentException("type");
		this.type = type;
	}
	
	
	public String getType() {
		return type;
	}
	
}
