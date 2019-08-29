package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = AccessApplication.class,
		properties = {
				"bcgov.access.input.plugin=http",
				"bcgov.access.output.plugin=rabbitmq"
		})
@ContextConfiguration
public class TopicExchangeTester {

	@Qualifier("documentReadyTopic")
	@Autowired
	private TopicExchange sut;
	
	@Test
	public void with_default_should_return_document_ready_topic() {
	
		assertEquals("document.ready", sut.getName());
		assertTrue(sut.isDurable());
		assertFalse(sut.isAutoDelete());
	
	}
}
