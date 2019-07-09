package ca.gov.bc.open.jrccaccess.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The RestAutoConfiguration configures the http plugin
 * @author alexjoybc
 * @since 0.2.0
 */
@Configuration
@ComponentScan("ca.gov.bc.open.jrccaccess.autoconfigure.rest")
public class RestAutoConfiguration {

}
