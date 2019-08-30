package ca.bc.gov.open.jrccaccess.autoconfigure.services;

import ca.bc.gov.open.jrccaccess.libs.DocumentOutput;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.processing.DocumentProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class DocumentReadyHandlerTests {

	private DocumentReadyHandler sut;
	
	@Mock
	private DocumentOutput documentOutput;
	
	@Mock
	private DocumentProcessor documentProcessor;
	
	@Mock
	private TransactionInfo transactionInfoMock;
	
	
	
	@Before
	public void init() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.documentOutput).send(Mockito.anyString(), Mockito.any());
		Mockito.when(documentProcessor.processDocument(Mockito.anyString(), Mockito.any())).thenReturn("processed");

	}
	
	
	@Test
	public void when_with_valid_input_no_processor_should_process() throws Exception {

		Optional<DocumentProcessor> documentProcessorOptional = Optional.empty();
		sut = new DocumentReadyHandler(documentOutput, documentProcessorOptional);
		sut.handle("awesome content", transactionInfoMock);
	}
	
	@Test
	public void when_with_valid_input_and_processor_should_process() throws Exception {

		Optional<DocumentProcessor> documentProcessorOptional = Optional.of(documentProcessor);
		sut = new DocumentReadyHandler(documentOutput, documentProcessorOptional);
		sut.handle("awesome content", transactionInfoMock);
	}
	
	
	
}
