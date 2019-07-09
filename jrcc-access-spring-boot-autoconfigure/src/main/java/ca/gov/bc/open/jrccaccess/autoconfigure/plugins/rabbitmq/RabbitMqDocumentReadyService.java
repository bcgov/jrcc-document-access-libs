package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyService;
import ca.gov.bc.open.jrccaccess.libs.services.ServiceUnavailableException;

/**
 * The RabbitMqDocumentReadyService provides services to interact with rabbitMq
 * @author alexjoybc
 * @since 0.2.0
 *
 */
@Service
@ConditionalOnProperty(name="bcgov.access.output.plugin", havingValue = "rabbitmq")
public class RabbitMqDocumentReadyService implements DocumentReadyService {

	@Qualifier("documentReadyTopicTemplate")
	@Autowired
	private RabbitTemplate documentReadyTopicTemplate;

	/**
	 * Publishes a document ready message to the desired Queue
	 */
	@Override
	public void Publish(DocumentReadyMessage message) throws ServiceUnavailableException {
		
		try {

			documentReadyTopicTemplate.convertAndSend(message);

		} catch (AmqpConnectException | AmqpIOException e) {

			throw new ServiceUnavailableException("rabbitMq service not available", e.getCause());

		} 
	}
}
