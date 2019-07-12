package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import ca.gov.bc.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;
import ca.gov.bc.open.jrccaccess.libs.services.ServiceUnavailableException;

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
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(documentReadyHandlerMock).Handle(Mockito.anyString(), Mockito.anyString());
		Mockito.doThrow(ServiceUnavailableException.class).when(documentReadyHandlerMock).Handle(Mockito.anyString(), Mockito.eq(SERVICE_UNAVAILABLE_EXCEPTION));
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
	
	
}
