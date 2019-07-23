package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessProperties;
import ca.gov.bc.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;
import ca.bc.gov.open.jrccaccess.libs.DocumentStorageProperties;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;

public class RabbitMqDocumentOutputTester {

	
	private RabbitMqDocumentOutput sut;
	
	@Mock
	private RabbitMqDocumentReadyService documentReadyService;
	
	@Mock
	private RedisStorageService storageService;
	
	@Before
	public void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.documentReadyService).publish(Mockito.any());
		Mockito.when(this.storageService.putString(Mockito.anyString())).thenReturn(new DocumentStorageProperties("key", "A1"));
		PluginConfig output = new PluginConfig();
		output.setDocumentType("mydoc");
		AccessProperties accessProperties = new AccessProperties();
		accessProperties.setOutput(output);
		this.sut = new RabbitMqDocumentOutput(this.storageService, this.documentReadyService, accessProperties);
	}
	
	@Test
	public void send_with_valid_input_should_store_and_publish_a_message() throws Exception {
		String content = "my awesome content";
		TransactionInfo transactionInfo = new TransactionInfo("testfile.txt", "me", LocalDateTime.now());
		this.sut.send(content, transactionInfo);
	}
	
}
