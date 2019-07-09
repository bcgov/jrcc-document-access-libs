package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import ca.gov.bc.open.jrccaccess.libs.DocumentInfo;
import ca.gov.bc.open.jrccaccess.libs.DocumentOutput;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;
import ca.gov.bc.open.jrccaccess.libs.services.ServiceUnavailableException;

/**
 * The rabbitMqDocumentOutput provides service to send document ready message 
 * to the desired Queue.
 * @author alexjoybc
 * @since 0.1.0
 */
@Service
@ConditionalOnProperty(name="bcgov.access.output.rabbitmq.document-type")
public class RabbitMqDocumentOutput implements DocumentOutput {

	private Logger logger = LoggerFactory.getLogger(RabbitMqDocumentOutput.class);
	
	private final RedisStorageService redisStorageService;
	private final RabbitMqDocumentReadyService rabbitMqDocumentReadyService;
	private final RabbitMqOutputProperties rabbitMqOutputProperties;
	
	/**
	 * Constructs a rabbitMq Document Output Service.
	 * @param redisStorageService	A {@link RedisStorageService}
	 * @param rabbitMqDocumentReadyService	A {@link RabbitMqDocumentReadyService}
	 */
	public RabbitMqDocumentOutput(RedisStorageService redisStorageService, RabbitMqDocumentReadyService rabbitMqDocumentReadyService, RabbitMqOutputProperties rabbitMqOutputProperties) {
		this.redisStorageService = redisStorageService;
		this.rabbitMqDocumentReadyService = rabbitMqDocumentReadyService;
		this.rabbitMqOutputProperties = rabbitMqOutputProperties;
	}
	
	/**
	 * Stores the document to redis and send a document ready message to rabbitMQ
	 */
	@Override
	public void send(String content, TransactionInfo transactionInfo) throws ServiceUnavailableException {
		
		DocumentInfo documentInfo = new DocumentInfo(rabbitMqOutputProperties.getDocumentType());
		
		logger.info("Attempting to publish [{}].", documentInfo);

		logger.debug("Attempting to store [{}] to redis.", documentInfo);
		DocumentStorageProperties documentStorageProperties = this.redisStorageService.putString(content);
		logger.info("[{}] successfully stored to redis key [{}].", documentInfo, documentStorageProperties.getKey());
		
		logger.debug("Creating new document ready message.");
		DocumentReadyMessage documentReadyMessage = new DocumentReadyMessage(transactionInfo, documentInfo, documentStorageProperties);
		
		logger.debug("Attempting to publish [{}] ready message to [{}] topic.", documentInfo, RabbitMqParam.DOCUMENT_READY_TOPIC);
		this.rabbitMqDocumentReadyService.Publish(documentReadyMessage);
		logger.info("[{}] successfully published to [{}] with [{}] routing key", documentInfo, RabbitMqParam.DOCUMENT_READY_TOPIC, rabbitMqOutputProperties.getDocumentType());

	}

	
	
	
	
	
}
