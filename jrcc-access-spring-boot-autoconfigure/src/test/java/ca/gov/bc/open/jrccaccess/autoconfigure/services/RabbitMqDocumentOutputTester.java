package ca.gov.bc.open.jrccaccess.autoconfigure.services;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessProperties;
import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;

public class RabbitMqDocumentOutputTester {

	
	private RabbitMqDocumentOutput sut;
	
	@Mock
	private RabbitMqDocumentReadyService documentReadyService;
	
	@Mock
	private RedisStorageService storageService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.documentReadyService).Publish(Mockito.any());
		Mockito.when(this.storageService.putString(Mockito.anyString())).thenReturn(new DocumentStorageProperties("key", "A1"));
		AccessProperties.Publish publish = new AccessProperties.Publish();
		publish.setDocumentType("mydoc");
		AccessProperties accessProperties = new AccessProperties();
		accessProperties.setPublish(publish);
		this.sut = new RabbitMqDocumentOutput(this.storageService, this.documentReadyService, accessProperties);
	}
	
	@Test
	public void send_with_valid_input_should_store_and_publish_a_message() {
		String content = "my awesome content";
		TransactionInfo transactionInfo = new TransactionInfo("testfile.txt", "me", LocalDateTime.now());
		this.sut.send(content, transactionInfo);
	}
	
}
