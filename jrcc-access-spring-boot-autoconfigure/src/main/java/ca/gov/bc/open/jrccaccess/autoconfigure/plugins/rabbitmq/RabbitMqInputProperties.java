package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
	

	/**
	 * Returns the delay between each retries
	 * @return
	 */
	public Integer getRetryDelay() {
		return this.retryDelay == null ? 0 : this.retryDelay;
	}

	/**
	 * Sets the delay between each retries
	 * @param retryDelay
	 */
	public void setRetryDelay(String retryDelay) {
		this.retryDelay = Integer.decode(retryDelay);;
	}
	
	

	
	
}
