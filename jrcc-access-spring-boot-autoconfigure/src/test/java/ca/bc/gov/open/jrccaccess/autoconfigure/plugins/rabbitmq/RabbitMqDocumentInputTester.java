package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import java.time.LocalDateTime;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties;
import ca.bc.gov.open.jrccaccess.autoconfigure.helpers.RandomHelper;
import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.open.jrccaccess.libs.DocumentReadyMessage;
import ca.bc.gov.open.jrccaccess.libs.DocumentStorageProperties;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentDigestMatchFailedException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;
import ca.bc.gov.open.jrccaccess.libs.utils.DigestUtils;

public class RabbitMqDocumentInputTester {


	private static final String SERVICE_UNAVAILABLE_EXCEPTION = "ServiceUnavailableException";

	private RabbitMqDocumentInput sut;
	
	private RabbitMqDocumentOutput sutOutput;
	
	@Mock
	private DocumentReadyMessage message;
	
	@Mock
	private DocumentReadyHandler documentReadyHandlerMock;
	
	@Mock
	private RabbitMqDocumentReadyService documentReadyService;
	
	@Mock
	private TransactionInfo transactionInfoMock;
	
	@Mock
	private RedisStorageService storageService;

	@Before
	public void init() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.documentReadyService).publish(Mockito.any());
		Mockito.doNothing().when(documentReadyHandlerMock).handle("message", transactionInfoMock);
		Mockito.doThrow(ServiceUnavailableException.class).when(documentReadyHandlerMock).handle("message", transactionInfoMock);
		Mockito.when(this.storageService.putString(Mockito.anyString())).thenReturn(new DocumentStorageProperties("key", "A1"));
		
		AccessProperties.PluginConfig output = new AccessProperties.PluginConfig();
		output.setDocumentType("mydoc");
		AccessProperties accessProperties = new AccessProperties();
		accessProperties.setOutput(output);
		
		sut = new RabbitMqDocumentInput(documentReadyHandlerMock, storageService);
		sutOutput = new RabbitMqDocumentOutput(this.storageService, this.documentReadyService, accessProperties);
	}
	
	@Test
	public void should_handle_input_document() throws DocumentMessageException {
		
		String key = RandomHelper.makeRandomString(10);
		String textContent = RandomHelper.makeRandomString(20);
		String md5 = DigestUtils.computeMd5(textContent);
		
		DocumentStorageProperties storageProperties = new DocumentStorageProperties(key, md5);
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		Mockito.when(this.message.getDocumentStorageProperties()).thenReturn(storageProperties);
		
		sut.receiveMessage(message);
		
	}
	
	@Test(expected = DocumentDigestMatchFailedException.class)
	public void when_getString_should_throw_DocumentDigestMatchFailedException() throws Exception {
		
		String key = RandomHelper.makeRandomString(10);
		String textContent = RandomHelper.makeRandomString(20);
		String md5 = DigestUtils.computeMd5(textContent + "Failed");
		
		DocumentStorageProperties storageProperties = new DocumentStorageProperties(key, md5);
		
		// we want to throw DocumentDigestMatchFailedException, which will then cause it to throw an AmqpRejectAndDontRequeueException
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		Mockito.when(this.message.getDocumentStorageProperties()).thenReturn(storageProperties);
		Mockito.when(this.storageService.putString(Mockito.anyString())).thenReturn(new DocumentStorageProperties(key, md5));
		Mockito.doThrow(DocumentDigestMatchFailedException.class).when(storageService).getString(Mockito.anyString(), Mockito.eq(md5));
		
		sut.receiveMessage(message);
		
	}
	
	
	@Test
	public void when_under_retry_limit_reach_should_process() throws DocumentMessageException {
		
		String key = RandomHelper.makeRandomString(10);
		String textContent = RandomHelper.makeRandomString(20);
		String md5 = DigestUtils.computeMd5(textContent);
		
		DocumentStorageProperties storageProperties = new DocumentStorageProperties(key, md5);
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		Mockito.when(this.message.getDocumentStorageProperties()).thenReturn(storageProperties);
	
		
		sut.receiveMessage(message);
		
	}
	
	@Test
	public void testPutAndGetDocumentFromStorage() throws DocumentMessageException {
		
		String key = RandomHelper.makeRandomString(10);
		String textContent = RandomHelper.makeRandomString(20);
		String md5 = DigestUtils.computeMd5(textContent);
		
		DocumentStorageProperties storageProperties = new DocumentStorageProperties(key, md5);
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		Mockito.when(this.message.getDocumentStorageProperties()).thenReturn(storageProperties);


		
		TransactionInfo transactionInfo = new TransactionInfo("testfile.txt", "me", LocalDateTime.now());

		this.sutOutput.send(textContent, transactionInfo);

		sut.receiveMessage(message);
		
	}
	


	
	
}
