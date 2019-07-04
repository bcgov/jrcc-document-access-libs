package ca.gov.bc.open.jrccaccess.autoconfigure.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyService;

@Service
public class RabbitMqDocumentReadyService implements DocumentReadyService {

	
	@Qualifier("documentReadyTopicTemplate")
	@Autowired
	private RabbitTemplate documentReadyTopicTemplate;
	
	public RabbitMqDocumentReadyService() {
		
	}
	
	@Override
	public void Publish(DocumentReadyMessage message) {
		
		documentReadyTopicTemplate.convertAndSend(message);
		
	}

}
