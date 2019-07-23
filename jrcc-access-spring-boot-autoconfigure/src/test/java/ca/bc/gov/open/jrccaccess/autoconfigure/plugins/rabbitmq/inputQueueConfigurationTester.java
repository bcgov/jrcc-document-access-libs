package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import static org.junit.Assert.assertEquals;
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
        		"spring.redis.cluster.nodes=127.0.0.1:5000,127.0.0.1:5001",
        		"bcgov.access.input.document-type=test-doc",
        		"bcgov.access.input.plugin=rabbitmq",
        		"bcgov.access.output.plugin=console"
        })
@ContextConfiguration
public class inputQueueConfigurationTester {

	@Autowired
	@Qualifier("documentReadyQueue")
	private Queue sut;
	
	@Test
	public void queue_with_default_config_should_return_queue() {
		
		assertTrue(sut.isDurable());
		assertEquals("test-doc.0s.x0.q", sut.getName());
		
	}
	
	
}
