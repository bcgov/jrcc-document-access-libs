package ca.gov.bc.open.jrccaccess.autoconfigure.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rabbitmq.client.RpcClient.Response;

import ca.bc.gov.open.api.model.DocumentReceivedResponse;
import ca.gov.bc.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.gov.bc.open.jrccaccess.libs.services.ServiceUnavailableException;

public class DocumentControllerTester {

	private static final String IO_EXCEPTION = "io_exception";
	
	private static final String SERVICE_UNAVAILABLE = "service_unavailable";

	private static final String VALID = "valid";

	private DocumentController sut;
	
	@Mock
	private DocumentReadyHandler documentReadyHandler;

	@Mock
	private Resource resource;
	
	@Mock
	private Resource resourceWithException;
	
	
	@Before
	public void init() throws IOException {
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.documentReadyHandler).Handle(Mockito.any(), Mockito.eq(VALID));
		Mockito.doThrow(new ServiceUnavailableException(SERVICE_UNAVAILABLE)).when(this.documentReadyHandler).Handle(Mockito.any(), Mockito.eq(SERVICE_UNAVAILABLE));
		Mockito.when(this.resourceWithException.getInputStream()).thenThrow(IOException.class);
		sut = new DocumentController(this.documentReadyHandler);
	}
	
	@Test
	public void post_with_valid_input_should_return_valid_response() {
		
		ResponseEntity<DocumentReceivedResponse> response = sut.postDocument(VALID, null, null, null, null, null, this.resource);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getAcknowledge());
		
	}

	@Test
	public void post_with_sevice_unavailable_input_should_return_503_response() {
		
		@SuppressWarnings("rawtypes")
		ResponseEntity response = sut.postDocument(SERVICE_UNAVAILABLE, null, null, null, null, null, this.resource);
		
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
