package ca.gov.bc.open.jrccaccess.autoconfigure.rabbitmq;

import static org.junit.Assert.assertEquals;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = AccessApplication.class,
		properties = {
				"bcgov.access.input.plugin=console",
				"bcgov.access.output.plugin=rabbitmq"
		})
@ContextConfiguration
public class MessageConverterTester {

	@Qualifier("jsonMessageConverter")
	@Autowired
	private MessageConverter sut;
	
	@Test
	public void with_default_should_return_jackson_converter() {
	
		assertEquals(Jackson2JsonMessageConverter.class, sut.getClass());

	}
}
