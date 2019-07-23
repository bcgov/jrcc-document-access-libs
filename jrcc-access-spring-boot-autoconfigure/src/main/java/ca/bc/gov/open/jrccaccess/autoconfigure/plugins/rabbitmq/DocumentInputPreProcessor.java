package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Default document input preprocessor. If the message retry limits exceed the policy it is putting the message in the parking lot queue.
 * @author 177226
 *
 */
@Component
@ConditionalOnProperty(name="bcgov.access.input.plugin", havingValue = "rabbitmq")
public class DocumentInputPreProcessor implements MessagePostProcessor {

	private RabbitMqInputProperties rabbitMqInputProperties;
	
	private Logger logger = LoggerFactory.getLogger(DocumentInputPreProcessor.class);
	
	public DocumentInputPreProcessor(RabbitMqInputProperties rabbitMqInputProperties) {
		this.rabbitMqInputProperties = rabbitMqInputProperties;
	}
	
	
	@Override
	public Message postProcessMessage(Message message) throws AmqpException {
		
		logger.debug("Message Pre Processor");
		
		if(message.getMessageProperties() == null) return message;
		
		List<Map<String, ?>> xDeathCollection = message.getMessageProperties().getXDeathHeader();
		
		if(xDeathCollection == null || xDeathCollection.isEmpty()) return message;
		
		Map<String, ?> xDeath = xDeathCollection.get(0);

		if(xDeath != null && 
				(Long)xDeath.get("count") > rabbitMqInputProperties.getRetryCount()) {	
			
			logger.error("Message has reach retry limit of {} retries", rabbitMqInputProperties.getRetryCount());
			throw new ImmediateAcknowledgeAmqpException("Message has reach retry limit.");
		}
		
		return message;

	}

}
