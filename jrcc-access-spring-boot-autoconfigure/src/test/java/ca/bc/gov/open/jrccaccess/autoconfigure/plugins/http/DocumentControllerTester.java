package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.bc.gov.open.api.model.DocumentReceivedResponse;
import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;

public class DocumentControllerTester {
	
	private static final String SERVICE_UNAVAILABLE = "service_unavailable";

	private static final String VALID = "valid";

	private DocumentController sut;
	
	@Mock
	private DocumentReadyHandler documentReadyHandler;

	@Mock
	private TransactionInfo transactionInfoMock;
	
	@Mock
	private Resource resourceWithException ;
	
	@Before
	public void init() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.documentReadyHandler).handle("message", this.transactionInfoMock);
		Mockito.doThrow(new ServiceUnavailableException(SERVICE_UNAVAILABLE)).when(this.documentReadyHandler).handle(Mockito.eq(SERVICE_UNAVAILABLE), Mockito.any());
		Mockito.doReturn("filename.txt").when(this.resourceWithException).getFilename();
		Mockito.when(this.resourceWithException.getInputStream()).thenThrow(IOException.class);
		sut = new DocumentController(this.documentReadyHandler);
	}
	
	@Test
	public void post_with_valid_input_should_return_valid_response() {
		ByteArrayResource bytes = new ByteArrayResource("awesome content".getBytes()){
			@Override
			public String getFilename(){
				return "documentController.txt";
			}
		};
		ResponseEntity<DocumentReceivedResponse> response = sut.postDocument(VALID, null, null, null, null, null, bytes);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getAcknowledge());
	}

	@Test
	public void post_with_sevice_unavailable_input_should_return_503_response() {
		ByteArrayResource bytes = new ByteArrayResource(SERVICE_UNAVAILABLE.getBytes()){
			@Override
			public String getFilename(){
				return "documentController.txt";
			}
		};
		@SuppressWarnings("rawtypes")
		ResponseEntity response = sut.postDocument(SERVICE_UNAVAILABLE, null, null, null, null, null, bytes);

		assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
		assertEquals(SERVICE_UNAVAILABLE, ((ca.bc.gov.open.api.model.Error)response.getBody()).getMessage());
		assertEquals(Integer.toString(HttpStatus.SERVICE_UNAVAILABLE.value()), ((ca.bc.gov.open.api.model.Error)response.getBody()).getCode());
		
	}
	
	@Test
	public void post_with_io_exception_input_should_return_500_response() {
		@SuppressWarnings("rawtypes")
		ResponseEntity response = sut.postDocument(VALID, null, null, null, null, null, this.resourceWithException);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ((ca.bc.gov.open.api.model.Error)response.getBody()).getMessage());
		assertEquals(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), ((ca.bc.gov.open.api.model.Error)response.getBody()).getCode());
		
	}
	
}
