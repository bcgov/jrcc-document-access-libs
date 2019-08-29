package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.libs.DocumentReadyMessage;
import ca.bc.gov.open.jrccaccess.libs.DocumentStorageProperties;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * The RabbitMqDocumentInput handles document from the rabbitMq message listener
 * 
 * @author alexjoybc
 * @since 0.4.0
 */
@Component
@ConditionalOnProperty(name = "bcgov.access.input.plugin", havingValue = "rabbitmq")
public class RabbitMqDocumentInput {

	private Logger logger = LoggerFactory.getLogger(RabbitMqDocumentInput.class);

	private DocumentReadyHandler documentReadyHandler;

	private final RedisStorageService redisStorageService;

	/**
	 * Creates a RabbitMqDocumentInput.
	 * 
	 * @param documentReadyHandler - A document ready handler.
	 */
	public RabbitMqDocumentInput(DocumentReadyHandler documentReadyHandler, RedisStorageService redisStorageService) {
		this.documentReadyHandler = documentReadyHandler;
		this.redisStorageService = redisStorageService;
	}

	/**
	 * Handles document ready messages. Retrieves the payload from temporary storage
	 * Handles retries sets in the message headers x-death.count and send to the
	 * documentReadyHandler.
	 * 
	 * @param documentReadyMessage
	 * @param xDeath
	 * @throws DocumentMessageException
	 */
	@RabbitListener(queues = "#{documentReadyQueue.getName()}")
	public void receiveMessage(DocumentReadyMessage documentReadyMessage) throws DocumentMessageException {

		logger.info("New Document Received {}", documentReadyMessage);

		DocumentStorageProperties storageProperties = documentReadyMessage.getDocumentStorageProperties();
		
		String content = this.redisStorageService.getString(storageProperties.getKey(), storageProperties.getMD5());
		
		if (logger.isDebugEnabled()) {
			logger.debug(content);
		}
		
		this.documentReadyHandler.handle(content, documentReadyMessage.getTransactionInfo());
		logger.info("message successfully acknowledged");

	}
}
