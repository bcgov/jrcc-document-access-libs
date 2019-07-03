package ca.gov.bc.open.jrccaccessspringbootsampleapp;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessProperties;
import ca.gov.bc.open.jrccaccess.libs.DocumentInfo;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyService;
import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.StorageService;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;


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
		
		@Autowired
		private AccessProperties accessProperties;
		
		@Autowired
		private DocumentReadyService documentReadyService;
		
		@Override
		public void run(String... args) throws Exception {
			logger.info("Starting access sample app");
			
			String content = "My awesome content";
			
			//stores document to redis
			DocumentStorageProperties props = storageService.putString(content);
			
			logger.info("content successfully stored in redis");
			
			logger.info("key: " + props.getKey());
			logger.info("MD5: " + props.getMD5());
			
			DocumentReadyMessage message = new DocumentReadyMessage(
					new TransactionInfo("testfile.txt", "jrcc-access-sample", LocalDateTime.now()), 
					new DocumentInfo(accessProperties.getPublish().getDocumentType()),
					props);
			
			//Sends document to the document ready topic
			documentReadyService.Publish(message);
			
			logger.info(MessageFormat.format("{0} successfully published", message));
		}
	}

}
