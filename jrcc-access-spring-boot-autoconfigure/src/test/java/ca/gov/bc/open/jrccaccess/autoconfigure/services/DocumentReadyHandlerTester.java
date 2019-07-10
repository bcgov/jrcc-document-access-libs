package ca.gov.bc.open.jrccaccess.autoconfigure.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.gov.bc.open.jrccaccess.libs.DocumentOutput;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;

public class DocumentReadyHandlerTester {

	private DocumentReadyHandler sut;
	
	@Mock
	private DocumentOutput documentOutput;
	
	@Mock
	private TransactionInfo transactionInfoMock;
	
	@Before
	public void init() {
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.documentOutput).send(Mockito.anyString(), Mockito.any());
		sut = new DocumentReadyHandler(documentOutput);
		
	}
	
	
	@Test
	public void send_with_valid_input_should_process() {
		

		sut.Handle("awesome content", "bcgov");

		
	}
	
	
	
}
