package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.http;

import ca.bc.gov.open.api.model.DocumentReceivedResponse;
import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties;
import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentControllerTests {

	private static final String SERVICE_UNAVAILABLE = "service_unavailable";

	private static final String VALID = "valid";

	private static final String FILENAME ="filename.txt";

	private DocumentController sut;
	
	@Mock
	private DocumentReadyHandler documentReadyHandler;

	@Mock
	private AccessProperties accessProperties;

	@Mock
    private PluginConfig pluginConfig;

	@Mock
	private TransactionInfo transactionInfoMock;
	
	@Mock
	private MultipartFile multipartFileWithException ;
	
	@Before
	public void init() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn(VALID);
		Mockito.when(this.transactionInfoMock.getFileName()).thenReturn(FILENAME);
		Mockito.when(this.transactionInfoMock.getReceivedOn()).thenReturn(LocalDateTime.now());
		Mockito.doNothing().when(this.documentReadyHandler).handle(Mockito.anyString(), Mockito.eq(this.transactionInfoMock));
		Mockito.doThrow(new ServiceUnavailableException(SERVICE_UNAVAILABLE)).when(this.documentReadyHandler).handle(Mockito.eq(SERVICE_UNAVAILABLE), Mockito.any());

		Mockito.doReturn(FILENAME).when(this.multipartFileWithException).getOriginalFilename();
		Mockito.when(this.multipartFileWithException.getInputStream()).thenThrow(IOException.class);

		Mockito.when(pluginConfig.getSender()).thenReturn(VALID);
		Mockito.when(accessProperties.getInput()).thenReturn(pluginConfig);

		sut = new DocumentController(this.documentReadyHandler, this.accessProperties);
	}
	
	@Test
	public void post_with_valid_input_should_return_valid_response() {
		MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
		Mockito.doReturn(FILENAME).when(multipartFile).getOriginalFilename();
		InputStream stream = new ByteArrayInputStream("awesome_content".getBytes(StandardCharsets.UTF_8));
		try {
			Mockito.doReturn(stream).when(multipartFile).getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ResponseEntity<DocumentReceivedResponse> response = sut.postDocument(null, null, null, null, null, multipartFile);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getAcknowledge());
	}

	@Test
	public void post_with_sevice_unavailable_input_should_return_503_response() {
		@SuppressWarnings("rawtypes")
		MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
		Mockito.doReturn(FILENAME).when(multipartFile).getOriginalFilename();
		InputStream stream = new ByteArrayInputStream(SERVICE_UNAVAILABLE.getBytes(StandardCharsets.UTF_8));

		Mockito.when(pluginConfig.getSender()).thenReturn(SERVICE_UNAVAILABLE);
		Mockito.when(accessProperties.getInput()).thenReturn(pluginConfig);

		try {
			Mockito.doReturn(stream).when(multipartFile).getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ResponseEntity response = sut.postDocument(null, null, null, null, null, multipartFile);

		assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
		assertEquals(SERVICE_UNAVAILABLE, ((ca.bc.gov.open.api.model.Error)response.getBody()).getMessage());
		assertEquals(Integer.toString(HttpStatus.SERVICE_UNAVAILABLE.value()), ((ca.bc.gov.open.api.model.Error)response.getBody()).getCode());
	}

	@Test
	public void post_with_io_exception_input_should_return_500_response() {
		@SuppressWarnings("rawtypes")
		ResponseEntity response = sut.postDocument(null, null, null, null, null, this.multipartFileWithException);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ((ca.bc.gov.open.api.model.Error)response.getBody()).getMessage());
		assertEquals(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), ((ca.bc.gov.open.api.model.Error)response.getBody()).getCode());
	}
}
