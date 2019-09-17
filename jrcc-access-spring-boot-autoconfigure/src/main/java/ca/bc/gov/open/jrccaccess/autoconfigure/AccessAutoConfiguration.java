package ca.bc.gov.open.jrccaccess.autoconfigure;


import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.InputConfigMissingException;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.InvalidConfigException;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.OutputConfigMissingException;
import ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq.RabbitMqOutputProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
	
	
	public AccessAutoConfiguration(AccessProperties accessProperties) throws InvalidConfigException {
		if( accessProperties.getOutput() == null){
			throw new OutputConfigMissingException();
		}
		if( accessProperties.getInput() == null){
			throw new InputConfigMissingException();
		}
		this.accessProperties = accessProperties;
		logger.info("Bootstraping Access Library");
		logger.info("Input plugin: {}", accessProperties.getInput().getPlugin());
		logger.info("Output plugin: {}", accessProperties.getOutput().getPlugin());
	}

	@Bean(name="inputConfig")
	public PluginConfig inputConfig() {
		return accessProperties.getInput();
	}
	
}
