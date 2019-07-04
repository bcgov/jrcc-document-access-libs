package ca.gov.bc.open.jrccaccess.autoconfigure.services;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessApplication;
import ca.gov.bc.open.jrccaccess.libs.DocumentInfo;
import ca.gov.bc.open.jrccaccess.libs.DocumentReadyMessage;
import ca.gov.bc.open.jrccaccess.libs.DocumentStorageProperties;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AccessApplication.class)
@ContextConfiguration
public class RabbitMqDocumentReadyServiceTester {

	
	@Mock
	private RabbitTemplate rabbitTemplateMock;
	

	@InjectMocks
	private RabbitMqDocumentReadyService sut;

	
	@Before
	public void init() {
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.rabbitTemplateMock).convertAndSend(Mockito.any());
		
	}
	
	@Test
	public void publish_with_valid_input_shoud_publish() {
		
		sut.Publish(new DocumentReadyMessage(new TransactionInfo("text.txt", "me", LocalDateTime.now()), new DocumentInfo("test"), new DocumentStorageProperties("key", "A1")));
		
	}
	
	
}
