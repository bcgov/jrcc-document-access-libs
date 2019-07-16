package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessProperties;
import ca.gov.bc.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;
import ca.gov.bc.open.jrccaccess.autoconfigure.helpers.RandomHelper;
import ca.gov.bc.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;
import ca.gov.bc.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import ca.gov.bc.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;
import ca.gov.bc.open.jrccaccess.libs.utils.DigestUtils;

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
		Mockito.doNothing().when(documentReadyHandlerMock).handle(Mockito.anyString(), Mockito.anyString());
		Mockito.doThrow(ServiceUnavailableException.class).when(documentReadyHandlerMock).handle(Mockito.anyString(), Mockito.eq(SERVICE_UNAVAILABLE_EXCEPTION));
		Mockito.when(this.storageService.putString(Mockito.anyString())).thenReturn(new DocumentStorageProperties("key", "A1"));
		
		PluginConfig output = new PluginConfig();
		output.setDocumentType("mydoc");
		AccessProperties accessProperties = new AccessProperties();
		accessProperties.setOutput(output);
		
		sut = new RabbitMqDocumentInput(documentReadyHandlerMock, storageService);
		sutOutput = new RabbitMqDocumentOutput(this.storageService, this.documentReadyService, accessProperties);
	}
	
	@Test
	public void should_handle_input_document() {
		
		String key = RandomHelper.makeRandomString(10);
		String textContent = RandomHelper.makeRandomString(20);
		String md5 = DigestUtils.computeMd5(textContent);
		
		DocumentStorageProperties storageProperties = new DocumentStorageProperties(key, md5);
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		Mockito.when(this.message.getDocumentStorageProperties()).thenReturn(storageProperties);
		
		sut.receiveMessage(message);
		
	}
	
	@Test(expected = AmqpRejectAndDontRequeueException.class)
	public void when_ServiceUnavailableException_should_throw_AmqpRejectAndDontRequeueException() throws Exception {
		
		String key = RandomHelper.makeRandomString(10);
		String textContent = RandomHelper.makeRandomString(20);
		String md5 = DigestUtils.computeMd5(textContent);
		
		DocumentStorageProperties storageProperties = new DocumentStorageProperties(key, md5);
		
		// we want to throw ServiceUnavailableException, which will then cause it to throw an AmqpRejectAndDontRequeueException
		Mockito.doThrow(ServiceUnavailableException.class).when(documentReadyHandlerMock).handle(Mockito.anyString(), Mockito.anyString());
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("DefaultSender");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		Mockito.when(this.message.getDocumentStorageProperties()).thenReturn(storageProperties);
		Mockito.when(this.storageService.putString(Mockito.anyString())).thenReturn(new DocumentStorageProperties(key, md5));
		Mockito.when(this.storageService.getString(Mockito.anyString(), Mockito.anyString())).thenReturn(textContent);
		
		sut = new RabbitMqDocumentInput(documentReadyHandlerMock, storageService);
		
		sut.receiveMessage(message);
		
	}
	
	
	@Test
	public void when_under_retry_limit_reach_should_process() {
		
		String key = RandomHelper.makeRandomString(10);
		String textContent = RandomHelper.makeRandomString(20);
		String md5 = DigestUtils.computeMd5(textContent);
		
		DocumentStorageProperties storageProperties = new DocumentStorageProperties(key, md5);
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		Mockito.when(this.message.getDocumentStorageProperties()).thenReturn(storageProperties);
		
		Map<Object, Object> xDeath = new HashMap<Object, Object>();
		
		xDeath.put("count", 2L);
		
		sut.receiveMessage(message);
		
	}
	
	@Test
	public void testPutAndGetDocumentFromStorage() {
		
		String key = RandomHelper.makeRandomString(10);
		String textContent = RandomHelper.makeRandomString(20);
		String md5 = DigestUtils.computeMd5(textContent);
		
		DocumentStorageProperties storageProperties = new DocumentStorageProperties(key, md5);
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		Mockito.when(this.message.getDocumentStorageProperties()).thenReturn(storageProperties);


		
		TransactionInfo transactionInfo = new TransactionInfo("testfile.txt", "me", LocalDateTime.now());
		try {
			this.sutOutput.send(textContent, transactionInfo);
		} catch (DocumentMessageException e) {
			e.printStackTrace();
		}
		
		sut.receiveMessage(message);
		
	}
	


	
	
}
