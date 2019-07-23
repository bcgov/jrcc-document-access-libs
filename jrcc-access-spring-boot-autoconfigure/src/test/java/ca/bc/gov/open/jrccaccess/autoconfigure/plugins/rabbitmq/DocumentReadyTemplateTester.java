package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = AccessApplication.class,
		properties = {
        		"spring.rabbitmq.host=rabbit",
        		"spring.rabbitmq.port=1234",
        		"bcgov.access.input.plugin=http",
    			"bcgov.access.output.plugin=rabbitmq",
        		"bcgov.access.output.document-type=test-doc"
        })
@ContextConfiguration
public class DocumentReadyTemplateTester {

	@Qualifier("documentReadyTopicTemplate")
	@Autowired
	private RabbitTemplate sut;
	
	@Test
	public void with_default_should_return_document_ready_template() {
	
		assertNotNull(sut);
		assertEquals("document.ready", sut.getExchange());
		assertEquals("test-doc", sut.getRoutingKey());
		assertEquals(Jackson2JsonMessageConverter.class, sut.getMessageConverter().getClass());
		assertEquals("rabbit", sut.getConnectionFactory().getHost());
		assertEquals(1234, sut.getConnectionFactory().getPort());
	
	}
}
