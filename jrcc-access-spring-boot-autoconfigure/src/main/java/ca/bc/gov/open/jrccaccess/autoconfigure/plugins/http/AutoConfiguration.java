package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.http;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The RestAutoConfiguration configures the http plugin
 * @author alexjoybc
 * @since 0.2.0
 */
@Configuration
@ComponentScan
@ConditionalOnExpression("'${bcgov.access.input.plugin}' == 'http'")
public class AutoConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register guest interceptor with single path pattern
        registry.addInterceptor(new DocumentInterceptor()).addPathPatterns("/document");

    }
}
