package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AccessApplication.class,
        properties = {
        		"bcgov.access.input.plugin=rabbitmq",
        		"bcgov.access.input.document-type=test-doc",
        		"bcgov.access.input.rabbitmq.retry-delay=5",
        		"bcgov.access.input.rabbitmq.retry-count=2",
        		"bcgov.access.output.plugin=console"
        })
@ContextConfiguration
public class DeadLetterQConfigurationTester {

	@Autowired
	@Qualifier("documentReadyDeadLetterQueue")
	private Queue sut;
	

	@Test
	public void with_default_config_should_return_a_valid_stringRedisTemplate() {

		assertTrue(sut.isDurable());
		assertEquals("test-doc.5s.x2.dlq", sut.getName());
		assertEquals(5000L, sut.getArguments().get(RabbitMqParam.X_MESSAGE_TTL_ARG));
		assertEquals("document.ready", sut.getArguments().get(RabbitMqParam.X_DEAD_LETTER_EXCHANGE_ARG));
		
	}
	

	
}
