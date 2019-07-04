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
import ca.gov.bc.open.jrccaccess.autoconfigure.services.RabbitMqDocumentOutput;
import ca.gov.bc.open.jrccaccess.libs.DocumentInfo;
import ca.gov.bc.open.jrccaccess.libs.DocumentOutput;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyService;
import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.StorageService;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;
import ca.gov.bc.open.jrccaccess.libs.services.ServiceUnavailableException;


@SpringBootApplication
public class JrccAccessSpringBootSampleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(JrccAccessSpringBootSampleAppApplication.class, args);
	}
	
	@Component
	public class ApplicationStartupRunner implements CommandLineRunner {
		
		protected final Logger logger = LoggerFactory.getLogger(ApplicationStartupRunner.class);
	
		@Autowired
		private DocumentOutput documentOutput;
		
		@Override
		public void run(String... args) throws Exception {
			logger.info("Starting access sample app");
			
			String content = "My awesome content";
			
			// Creates a new transaction
			TransactionInfo transactionInfo = new TransactionInfo("testfile.txt", "jrcc-access-sample", LocalDateTime.now());
			
			try {
				// Send the content to redis and rabbit
				this.documentOutput.send(content, transactionInfo); 
				logger.info("Successfully store and send message");
			
			} catch(ServiceUnavailableException e) {
				logger.error(e.getMessage());
			}

		}
	}

}
