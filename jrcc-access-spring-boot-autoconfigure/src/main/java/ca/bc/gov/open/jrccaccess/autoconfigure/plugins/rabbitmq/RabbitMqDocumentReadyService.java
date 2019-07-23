package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties;
import ca.bc.gov.open.jrccaccess.libs.DocumentReadyMessage;
import ca.bc.gov.open.jrccaccess.libs.DocumentReadyService;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;

/**
 * The RabbitMqDocumentReadyService provides services to interact with rabbitMq
 * 
 * @author alexjoybc
 * @since 0.2.0
 *
 */
@Service
@ConditionalOnProperty(name = "bcgov.access.output.plugin", havingValue = "rabbitmq")
public class RabbitMqDocumentReadyService implements DocumentReadyService {

	@Qualifier("documentReadyTopicTemplate")
	@Autowired
	private RabbitTemplate documentReadyTopicTemplate;

	@Autowired
	private AccessProperties accessProperties;

	/**
	 * Publishes a document ready message to the desired Queue
	 */
	@Override
	public void publish(DocumentReadyMessage message) throws ServiceUnavailableException {

		try {

			documentReadyTopicTemplate.convertAndSend(message, m -> {
				m.getMessageProperties().getHeaders().put(RabbitMqParam.X_DEAD_LETTER_ROUTING_KEY,
						accessProperties.getOutput().getDocumentType());
				return m;
			});

		} catch (AmqpConnectException | AmqpIOException e) {

			throw new ServiceUnavailableException("rabbitMq service not available", e.getCause());

		}
	}
}
