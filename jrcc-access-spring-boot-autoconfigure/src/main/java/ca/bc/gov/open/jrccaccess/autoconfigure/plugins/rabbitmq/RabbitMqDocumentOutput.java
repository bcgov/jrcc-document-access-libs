package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties;
import ca.bc.gov.open.jrccaccess.autoconfigure.redis.RedisStorageService;
import ca.bc.gov.open.jrccaccess.libs.*;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * The rabbitMqDocumentOutput provides service to send document ready message 
 * to the desired Queue.
 * @author alexjoybc
 * @since 0.1.0
 */
@Service
@ConditionalOnProperty(name="bcgov.access.output.plugin", havingValue = "rabbitmq")
public class RabbitMqDocumentOutput implements DocumentOutput {

	private Logger logger = LoggerFactory.getLogger(RabbitMqDocumentOutput.class);
	
	private final StorageService storageService;
	private final RabbitMqDocumentReadyService rabbitMqDocumentReadyService;
	private final AccessProperties accessProperties;
	
	/**
	 * Constructs a rabbitMq Document Output Service.
	 * @param storageService	A {@link StorageService}
	 * @param rabbitMqDocumentReadyService	A {@link RabbitMqDocumentReadyService}
	 */
	public RabbitMqDocumentOutput(StorageService storageService, RabbitMqDocumentReadyService rabbitMqDocumentReadyService, AccessProperties accessProperties) {
		this.storageService = storageService;
		this.rabbitMqDocumentReadyService = rabbitMqDocumentReadyService;
		this.accessProperties = accessProperties;
	}
	
	/**
	 * Stores the document to redis and send a document ready message to rabbitMQ
	 */
	@Override
	public void send(String content, TransactionInfo transactionInfo) throws DocumentMessageException {
		
		DocumentInfo documentInfo = new DocumentInfo(accessProperties.getOutput().getDocumentType());
		
		logger.info("Attempting to publish [{}].", documentInfo);

		logger.debug("Attempting to store [{}] to redis.", documentInfo);
		DocumentStorageProperties documentStorageProperties = this.storageService.putString(content);
		logger.info("[{}] successfully stored to redis key [{}].", documentInfo, documentStorageProperties.getKey());
		
		logger.debug("Creating new document ready message.");
		DocumentReadyMessage documentReadyMessage = new DocumentReadyMessage(transactionInfo, documentInfo, documentStorageProperties);
		
		logger.debug("Attempting to publish [{}] ready message to [{}] topic.", documentInfo, RabbitMqParam.DOCUMENT_READY_TOPIC);

		this.rabbitMqDocumentReadyService.publish(documentReadyMessage);
		logger.info("[{}] successfully published to [{}] with [{}] routing key", documentInfo, RabbitMqParam.DOCUMENT_READY_TOPIC, accessProperties.getOutput().getDocumentType());

	}

	
	
	
	
	
}
