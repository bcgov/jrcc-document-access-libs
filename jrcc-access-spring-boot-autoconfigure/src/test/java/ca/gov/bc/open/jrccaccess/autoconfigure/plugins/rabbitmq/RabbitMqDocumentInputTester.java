package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessProperties;
import ca.gov.bc.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;
import ca.gov.bc.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;
import ca.gov.bc.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;

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
	private RabbitMqInputProperties rabbitMqInputProperties;
	
	@Mock
	private RedisStorageService storageService;
	
	@Before
	public void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.documentReadyService).Publish(Mockito.any());
		Mockito.doNothing().when(documentReadyHandlerMock).handle(Mockito.anyString(), Mockito.anyString());
		Mockito.doThrow(ServiceUnavailableException.class).when(documentReadyHandlerMock).handle(Mockito.anyString(), Mockito.eq(SERVICE_UNAVAILABLE_EXCEPTION));
		Mockito.when(this.storageService.putString(Mockito.anyString())).thenReturn(new DocumentStorageProperties("key", "A1"));
		Mockito.when(rabbitMqInputProperties.getRetryCount()).thenReturn(3);
		
		PluginConfig output = new PluginConfig();
		output.setDocumentType("mydoc");
		AccessProperties accessProperties = new AccessProperties();
		accessProperties.setOutput(output);
		
		sut = new RabbitMqDocumentInput(documentReadyHandlerMock, rabbitMqInputProperties, storageService);
		sutOutput = new RabbitMqDocumentOutput(this.storageService, this.documentReadyService, accessProperties);
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
	
	@Test(expected = ImmediateAcknowledgeAmqpException.class)
	public void when_retry_limit_reach_should_throw_ImmediateAcknowledgeAmqpException() {
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		
		Map<Object, Object> xDeath = new HashMap<Object, Object>();
		
		xDeath.put("count", 4L);
		
		sut.receiveMessage(message, xDeath);
		
	}
	
	@Test
	public void when_under_retry_limit_reach_should_process() {
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		
		Map<Object, Object> xDeath = new HashMap<Object, Object>();
		
		xDeath.put("count", 2L);
		
		sut.receiveMessage(message, xDeath);
		
	}
	
	@Test
	public void testPutAndGetDocumentFromStorage() {
		
		String key = "key123";
		String textContent = "Testing123";
		String md5 = computeMd5("Testing123");
		
		DocumentStorageProperties storageProperties = new DocumentStorageProperties(key, md5);
		
		Mockito.when(this.transactionInfoMock.getSender()).thenReturn("bcgov");
		Mockito.when(this.message.getTransactionInfo()).thenReturn(transactionInfoMock);
		Mockito.when(this.message.getDocumentStorageProperties()).thenReturn(storageProperties);


		
		TransactionInfo transactionInfo = new TransactionInfo("testfile.txt", "me", LocalDateTime.now());
		try {
			this.sutOutput.send(textContent, transactionInfo);
		} catch (ServiceUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sut.receiveMessage(message, null);
		
	}
	

	private String computeMd5(String content) {

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(content.getBytes(StandardCharsets.UTF_8));
			return DatatypeConverter.printHexBinary(md.digest());
		} catch (NoSuchAlgorithmException e) {
			// can't happen
			e.printStackTrace();
			return "";
		}

	}
	
	
}
