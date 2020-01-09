package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;


import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

public class DocumentInputErrorHandlerTests {

	
	private DocumentInputErrorHandler sut;
	
	
	@Before
	public void init() {
		sut = new DocumentInputErrorHandler();
	}
	
	@Test(expected = AmqpRejectAndDontRequeueException.class)
	public void with_serviceUnavailable_should_throw_AmqpRejectAndDontRequeueException() {
		sut.handleError(new ListenerExecutionFailedException("test", new ServiceUnavailableException("test"), new Message("test".getBytes(), null)));
	}
	
	@Test(expected = ImmediateAcknowledgeAmqpException.class)
	public void with_otherException_should_throw_ImmediateAcknowledgeAmqpException() {
		
		sut.handleError(new ListenerExecutionFailedException("test", new Exception("test"), new Message("test".getBytes(), null)));
		
	}
	
	@Test(expected = ImmediateAcknowledgeAmqpException.class)
	public void with_exception_should_throw_ImmediateAcknowledgeAmqpException() {
		
		sut.handleError(new Exception("test"));
		
	}
	
	
}
