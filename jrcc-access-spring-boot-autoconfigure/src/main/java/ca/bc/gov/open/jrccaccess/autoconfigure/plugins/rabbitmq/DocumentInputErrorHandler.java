package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

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
		
		logger.debug("Handling error thrown by rabbitmq listener.");
		
		if(!(t instanceof ListenerExecutionFailedException)) {
			logger.error("Error not instance of ListenerExecutionFailedException, the message will be acknowledged and discard from the queue.");
			MDC.clear();
			throw new ImmediateAcknowledgeAmqpException(t);
		}
		
		ListenerExecutionFailedException actualException = (ListenerExecutionFailedException)t;
		
		if(actualException.getCause() instanceof ServiceUnavailableException) {
			logger.warn("Service Unavailable Exception: the message will be rejected and move to the corresponding dead letter queue.");
			MDC.clear();
			throw new AmqpRejectAndDontRequeueException(t);
		}
		
		logger.error("{}: the message will be acknowledged and discard from the queue.", t.getMessage());
		MDC.clear();
		throw new ImmediateAcknowledgeAmqpException(t);
		
	}

}
