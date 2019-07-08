package ca.gov.bc.open.jrccaccess.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AccessProperties.class)
@ComponentScan("ca.gov.bc.open.jrccaccess.autoconfigure.rest")
public class RestAutoConfiguration {

}
