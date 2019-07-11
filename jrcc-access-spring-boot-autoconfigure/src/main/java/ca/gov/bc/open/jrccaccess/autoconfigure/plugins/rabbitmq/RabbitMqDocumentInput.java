package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;

@Component
public class RabbitMqDocumentInput {

	
	private Logger logger = LoggerFactory.getLogger(RabbitMqDocumentInput.class);
	
	
	public void receiveMessage(DocumentReadyMessage documentReadyMessage) {
		
		logger.info("New Document Received {}", documentReadyMessage);
		
	}
	
	
}
