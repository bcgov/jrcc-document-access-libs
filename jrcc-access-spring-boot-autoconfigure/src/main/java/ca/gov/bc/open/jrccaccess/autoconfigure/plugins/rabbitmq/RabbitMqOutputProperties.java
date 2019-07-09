package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents the rabbitmq input plugin properties
 * @author alexjoybc
 * @since 0.3.0
 *
 */
@ConfigurationProperties(prefix = "bcgov.access.output.rabbitmq")
public class RabbitMqOutputProperties {
	
	@Min(0)
	private Integer ttl;
	
	private String documentType;
	
	/**
	 * 
	 * @return the Time to live set for the temporary storage.
	 */
	public Integer getTtl() {
		return this.ttl == null ? 1 : this.ttl;
	}

	/**
	 * Sets the time to live for the temporary storage.
	 * @param ttl
	 */
	public void setTtl(String ttl) {
		this.ttl = Integer.decode(ttl);
	}
	
	/**
	 * Sets the document type to publish
	 * @param documentType
	 */
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	/**
	 * Gets the document type to publish
	 * @return
	 */
	public String getDocumentType() {
		return documentType;
	}

	
	
	
}
