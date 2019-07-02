package ca.gov.bc.open.jrccaccessspringbootsampleapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.StorageService;


@SpringBootApplication
public class JrccAccessSpringBootSampleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(JrccAccessSpringBootSampleAppApplication.class, args);
	}
	
	@Component
	public class ApplicationStartupRunner implements CommandLineRunner {
		
		protected final Logger logger = LoggerFactory.getLogger(ApplicationStartupRunner.class);
	
		@Autowired
		private StorageService storageService;
		
		
		@Override
		public void run(String... args) throws Exception {
			logger.info("Starting access sample app");
			
			String content = "My awesome content";
			
			DocumentStorageProperties props = storageService.putString(content);
			
			logger.info("content successfully stored in redis");
			
			logger.info("key: " + props.getKey());
			logger.info("MD5: " + props.getMD5());
			
			
		}
	}

}
