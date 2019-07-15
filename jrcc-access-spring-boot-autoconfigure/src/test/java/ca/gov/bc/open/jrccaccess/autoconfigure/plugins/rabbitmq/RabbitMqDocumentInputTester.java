package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;

import ca.gov.bc.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;
import ca.gov.bc.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;

public class RabbitMqDocumentInputTester {


	private static final String SERVICE_UNAVAILABLE_EXCEPTION = "ServiceUnavailableException";

	private RabbitMqDocumentInput sut;
	
	@Mock
	private DocumentReadyMessage message;
	
	@Mock
	private DocumentReadyHandler documentReadyHandlerMock;
	
	@Mock
	private TransactionInfo transactionInfoMock;
	
	@Mock 
	private RabbitMqInputProperties rabbitMqInputProperties;
	
	@Before
	public void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(documentReadyHandlerMock).handle(Mockito.anyString(), Mockito.anyString());
		Mockito.doThrow(ServiceUnavailableException.class).when(documentReadyHandlerMock).handle(Mockito.anyString(), Mockito.eq(SERVICE_UNAVAILABLE_EXCEPTION));
		Mockito.when(rabbitMqInputProperties.getRetryCount()).thenReturn(3);
		sut = new RabbitMqDocumentInput(documentReadyHandlerMock, rabbitMqInputProperties);
	}
	
	@Test
	public void should_handle_input_document() {
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		
		sut.receiveMessage(message, null);
		
	}
	
	@Test(expected = AmqpRejectAndDontRequeueException.class)
	public void when_ServiceUnavailableException_should_throw_AmqpRejectAndDontRequeueException() {
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn(SERVICE_UNAVAILABLE_EXCEPTION);
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		
		sut.receiveMessage(message, null);
		
	}
	
	@Test(expected = ImmediateAcknowledgeAmqpException.class)
	public void when_retry_limit_reach_should_throw_ImmediateAcknowledgeAmqpException() {
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		
		Map<Object, Object> xDeath = new HashMap<Object, Object>();
		
		xDeath.put("count", 4L);
		
		sut.receiveMessage(message, xDeath);
		
	}
	
	@Test
	public void when_under_retry_limit_reach_should_process() {
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		
		Map<Object, Object> xDeath = new HashMap<Object, Object>();
		
		xDeath.put("count", 2L);
		
		sut.receiveMessage(message, xDeath);
		
	}
	
	
	
}
