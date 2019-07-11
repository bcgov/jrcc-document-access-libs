package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Component;

import ca.gov.bc.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.services.ServiceUnavailableException;

/**
 * The RabbitMqDocumentInput handles document from the rabbitMq message listener
 * 
 * @author alexjoybc
 * @since 0.4.0
 */
@Component
public class RabbitMqDocumentInput {

	private Logger logger = LoggerFactory.getLogger(RabbitMqDocumentInput.class);

	private DocumentReadyHandler documentReadyHandler;

	/**
	 * Creates a RabbitMqDocumentInput.
	 * 
	 * @param documentReadyHandler - A document ready handler.
	 */
	public RabbitMqDocumentInput(DocumentReadyHandler documentReadyHandler) {
		this.documentReadyHandler = documentReadyHandler;
	}

	/**
	 * Handles document ready messages. Retrieves the payload from temporary storage
	 * and send to the documentReadyHandler.
	 * 
	 * @param documentReadyMessage
	 */
	public void receiveMessage(DocumentReadyMessage documentReadyMessage) {

		try {

			logger.info("New Document Received {}", documentReadyMessage);
			
			Integer test = 800 / 0;
			
			this.documentReadyHandler.Handle("not implemented yet",
					documentReadyMessage.getTransactionInfo().getSender());
		
		} catch (ServiceUnavailableException e) {
			
			logger.warn("Service unavailable exception, message will be put into the dead letter queue.");
			throw new AmqpRejectAndDontRequeueException("blah");
			
		} catch (Exception e) {
			
			logger.error(e.getMessage());
			throw new AmqpRejectAndDontRequeueException("blah");
			// TODO: move message to parking lot queue.
		}
	}

}
