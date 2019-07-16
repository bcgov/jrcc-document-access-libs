package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler.DefaultExceptionStrategy;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import ca.gov.bc.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;

/**
 * Document input error handler
 * @author alexjoy
 *
 */
@Component
@ConditionalOnProperty(name = "bcgov.access.input.plugin", havingValue = "rabbitmq")
public class DocumentInputErrorHandler implements ErrorHandler  {

	private Logger logger = LoggerFactory.getLogger(DocumentInputErrorHandler.class);

	@Override
	public void handleError(Throwable t) {
		
		if(!(t instanceof ListenerExecutionFailedException)) throw new ImmediateAcknowledgeAmqpException(t);
		
		ListenerExecutionFailedException actualException = (ListenerExecutionFailedException)t;
		
		if(actualException.getCause() instanceof ServiceUnavailableException) throw new AmqpRejectAndDontRequeueException(t);
		
		throw new ImmediateAcknowledgeAmqpException(t);
		
	}

}
