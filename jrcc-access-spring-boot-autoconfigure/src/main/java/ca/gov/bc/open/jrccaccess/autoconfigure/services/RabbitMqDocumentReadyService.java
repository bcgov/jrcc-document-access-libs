package ca.gov.bc.open.jrccaccess.autoconfigure.services;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyService;
import ca.gov.bc.open.jrccaccess.libs.services.ServiceUnavailableException;

@Service
public class RabbitMqDocumentReadyService implements DocumentReadyService {

	@Qualifier("documentReadyTopicTemplate")
	@Autowired
	private RabbitTemplate documentReadyTopicTemplate;

	@Override
	public void Publish(DocumentReadyMessage message) throws ServiceUnavailableException {

		try {

			documentReadyTopicTemplate.convertAndSend(message);

		} catch (AmqpConnectException | AmqpIOException e) {

			throw new ServiceUnavailableException("rabbitMq service not available", e.getCause());

		} 
	}
}
