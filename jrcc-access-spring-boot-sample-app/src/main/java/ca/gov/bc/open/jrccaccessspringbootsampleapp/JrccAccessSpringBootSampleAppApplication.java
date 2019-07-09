package ca.gov.bc.open.jrccaccessspringbootsampleapp;


import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class JrccAccessSpringBootSampleAppApplication {

	
	private Logger logger = LoggerFactory.getLogger(JrccAccessSpringBootSampleAppApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(JrccAccessSpringBootSampleAppApplication.class, args);
	}
	
	@Bean
	@ConditionalOnProperty(
			name="logging.level.ca.gov.bc", 
			havingValue="DEBUG")
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("");
            logger.debug("########### Spring Boot Beans #############");

            String[] beanNames = ctx.getBeanDefinitionNames();
            
            logger.debug("{} beans registerd", beanNames.length);
           
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
            	logger.debug(beanName);
            }
            
            logger.debug("############################################");

        };
    }

}
