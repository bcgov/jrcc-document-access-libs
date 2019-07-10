package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;

public class RabbitMqDocumentInputTester {

	private RabbitMqDocumentInput sut;
	
	@Mock
	private DocumentReadyMessage message;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		sut = new RabbitMqDocumentInput();
	}
	
	@Test
	public void should_handle_input_document() {
		
		sut.receiveMessage(message);
		
	}
	
	
}
