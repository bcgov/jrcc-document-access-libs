package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.gov.bc.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;

public class RabbitMqDocumentInputTester {

	private RabbitMqDocumentInput sut;
	
	@Mock
	private DocumentReadyMessage message;
	
	@Mock
	private DocumentReadyHandler documentReadyHandlerMock;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(documentReadyHandlerMock).Handle(Mockito.anyString(), Mockito.anyString());
		sut = new RabbitMqDocumentInput(documentReadyHandlerMock);
	}
	
	@Test
	public void should_handle_input_document() {
		
		sut.receiveMessage(message);
		
	}
	
	
}
