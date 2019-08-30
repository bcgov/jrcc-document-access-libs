package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessApplication;
import ca.bc.gov.open.jrccaccess.libs.DocumentInfo;
import ca.bc.gov.open.jrccaccess.libs.DocumentReadyMessage;
import ca.bc.gov.open.jrccaccess.libs.DocumentStorageProperties;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AccessApplication.class,
        properties = {
        		"bcgov.access.input.plugin=http",
        		"bcgov.access.output.plugin=rabbitmq"
        })
@ContextConfiguration
public class RabbitMqDocumentReadyServiceTests {

	
	private static final DocumentReadyMessage MESSAGE_1 = new DocumentReadyMessage(new TransactionInfo("text.txt", "me", LocalDateTime.now()), new DocumentInfo("test"), new DocumentStorageProperties("key", "A1"));
	private static final DocumentReadyMessage MESSAGE_2 = new DocumentReadyMessage(new TransactionInfo("AmqpConnectException.txt", "me", LocalDateTime.now()), new DocumentInfo("test"), new DocumentStorageProperties("key", "A1"));
	private static final DocumentReadyMessage MESSAGE_3 = new DocumentReadyMessage(new TransactionInfo("AmqpIOException.txt", "me", LocalDateTime.now()), new DocumentInfo("test"), new DocumentStorageProperties("key", "A1"));



	@Mock
	private RabbitTemplate rabbitTemplateMock;

	@InjectMocks
	private RabbitMqDocumentReadyService sut;

	
	
	@Before
	public void init() {
		
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.rabbitTemplateMock).convertAndSend(Mockito.eq(MESSAGE_1), Mockito.any());
		Mockito.doThrow(AmqpConnectException.class).when(this.rabbitTemplateMock).convertAndSend(Mockito.eq(MESSAGE_2), Mockito.any());
		Mockito.doThrow(AmqpIOException.class).when(this.rabbitTemplateMock).convertAndSend(Mockito.eq(MESSAGE_3), Mockito.any());
		
	}
	
	@Test
	public void publish_with_valid_input_shoud_publish() throws Exception {
		
		sut.publish(MESSAGE_1);
		
	}
	
	@Test(expected = ServiceUnavailableException.class)
	public void publish_with_AmqpConnectException_shoud_throw_ServiceUnavailableException() throws Exception {
		
		sut.publish(MESSAGE_2);
		
	}
	
	@Test(expected = ServiceUnavailableException.class)
	public void publish_with_AmqpIOException_shoud_throw_ServiceUnavailableException() throws Exception {
		
		sut.publish(MESSAGE_3);
		
	}
	
	
}
