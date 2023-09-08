package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.Min;

/**
 * Represents the rabbitmq output plugin properties
 * @author alexjoybc
 * @since 0.3.0
 *
 */
@ConfigurationProperties(prefix = "bcgov.access.input.rabbitmq")
public class RabbitMqInputProperties {
	

	@Min(0)
	private Integer retryDelay;
	
	@Min(0)
	private Integer retryCount;

	/**
	 * Returns the delay between each retries
	 * @return
	 */
	public Integer getRetryDelay() {
		return this.retryDelay == null ? 0 : this.retryDelay;
	}
	
	/**
	 * Returns the total # of retries
	 * @return
	 */
	public Integer getRetryCount() {
		return retryCount == null ? 0 : this.retryCount;
	}

	/**
	 * Sets the delay between each retries
	 * @param retryDelay
	 */
	public void setRetryDelay(String retryDelay) {
		this.retryDelay = Integer.decode(retryDelay);
	}
	
	/**
	 * Sets the total retries
	 * @param retryCount
	 */
	public void setRetryCount(String retryCount) {
		this.retryCount = Integer.decode(retryCount);
	}

	
	
}
