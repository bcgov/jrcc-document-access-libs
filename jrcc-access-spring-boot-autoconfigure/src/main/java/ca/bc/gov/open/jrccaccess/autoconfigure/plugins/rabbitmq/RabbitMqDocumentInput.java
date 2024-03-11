package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import ca.bc.gov.open.jrccaccess.autoconfigure.common.Constants;
import ca.bc.gov.open.jrccaccess.autoconfigure.redis.RedisStorageService;
import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.libs.DocumentReadyMessage;
import ca.bc.gov.open.jrccaccess.libs.DocumentStorageProperties;
import ca.bc.gov.open.jrccaccess.libs.StorageService;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

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

	private final StorageService storageService;

	/**
	 * Creates a RabbitMqDocumentInput.
	 * 
	 * @param documentReadyHandler - A document ready handler.
	 */
	public RabbitMqDocumentInput(DocumentReadyHandler documentReadyHandler, StorageService storageService) {
		this.documentReadyHandler = documentReadyHandler;
		this.storageService = storageService;
	}

	/**
	 * Handles document ready messages. Retrieves the payload from temporary storage
	 * Handles retries sets in the message headers x-death.count and send to the
	 * documentReadyHandler.
	 * 
	 * @param documentReadyMessage
	 * @throws DocumentMessageException
	 */
	@RabbitListener(queues = "#{documentReadyQueue.getName()}")
	public void receiveMessage(DocumentReadyMessage documentReadyMessage) throws DocumentMessageException {
		MDC.put(Constants.MDC_KEY_FILENAME, documentReadyMessage.getTransactionInfo().getFileName());
		logger.info("New Document Received {}", documentReadyMessage);

		DocumentStorageProperties storageProperties = documentReadyMessage.getDocumentStorageProperties();
		
		String content = this.storageService.getString(storageProperties.getKey(), storageProperties.getDigest());
		
		if (logger.isDebugEnabled()) {
			logger.debug(content);
		}


		this.documentReadyHandler.handle(content, documentReadyMessage.getTransactionInfo());

        logger.debug("attempting to delete the document from redis storage");

        //when successfully processed, delete the document from redis storage
        this.storageService.deleteString(storageProperties.getKey());

        logger.info("document successfully deleted from redis storage");
		logger.info("message successfully acknowledged");
		MDC.clear();
	}
}
