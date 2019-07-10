package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AccessApplication.class,
        properties = {
        		"spring.redis.cluster.nodes=127.0.0.1:5000,127.0.0.1:5001",
        		"bcgov.access.input.document-type=test-doc",
        		"bcgov.access.input.plugin=rabbitmq",
        		"bcgov.access.output.plugin=console"
        })
@ContextConfiguration
public class ListenerAdapterConfigurationTester {

	@Autowired
	private MessageListenerAdapter sut;
	
	@Test
	public void should_return_a_listenerAdpater() {
		assertNotNull(sut);
	}
	
}
