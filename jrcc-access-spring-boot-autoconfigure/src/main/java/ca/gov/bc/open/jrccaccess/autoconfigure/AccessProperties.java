package ca.gov.bc.open.jrccaccess.autoconfigure;

import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents the custom configuration for the document flow lib
 * @author 177226
 *
 */
@ConfigurationProperties(prefix = "bcgov.access")
public class AccessProperties {
	
	@Min(0)
	private Integer ttl;
	
	private String input;
	
	private String output;
	
	/**
	 * the public details
	 * @author alexjoybc
	 */
	private Publish publish;
	
	public Integer getTtl() {
		return this.ttl == null ? 1 : this.ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = Integer.decode(ttl);
	}

	
	public Publish getPublish() {
		if(publish == null)  this.publish = new Publish();
		return publish;
	}

	public void setPublish(Publish publish) {
		this.publish = publish;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * Represents the publish details
	 * @author alexjoybc
	 *
	 */
	public static  class Publish {
		
		private String documentType;

		public String getDocumentType() {
			if (documentType == null || documentType.isEmpty()) return "unknown";
			return documentType;
		}

		public void setDocumentType(String documentType) {
			this.documentType = documentType;
		}

	}
	

	
}
