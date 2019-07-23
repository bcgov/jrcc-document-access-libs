package ca.bc.gov.open.jrccaccess.autoconfigure;


import ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq.RabbitMqOutputProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The AccessAutoConfiguration is the default configuration for the access library
 * @author alexjoybc
 * @since 0.0.1
 */
@Configuration
@EnableConfigurationProperties({ RabbitMqOutputProperties.class, AccessProperties.class })
@ComponentScan("ca.bc.gov.open.jrccaccess.autoconfigure.services")
public class AccessAutoConfiguration {

	@SuppressWarnings("unused")
	private AccessProperties accessProperties;
	private Logger logger = LoggerFactory.getLogger(AccessAutoConfiguration.class);
	
	
	public AccessAutoConfiguration(AccessProperties accessProperties) {
		this.accessProperties = accessProperties;
		logger.info("Bootstraping Access Library", accessProperties.getOutput().getPlugin());
		logger.info("Input plugin: {}", accessProperties.getInput().getPlugin());
		logger.info("Output plugin: {}", accessProperties.getOutput().getPlugin());
	}
	
}
