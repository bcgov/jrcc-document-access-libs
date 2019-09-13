package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AccessApplication.class,
        properties = {
        		"bcgov.access.input.plugin=http",
        		"bcgov.access.output.plugin=rabbitmq"
        })
@ContextConfiguration
public class ConnectionFactoryIT {

	@Autowired
	private ConnectionFactory sut;
	
	@Test
	public void should_return_a_valid_connection_factory() {
		
		
		assertEquals("localhost", sut.getHost());
		assertEquals(5672, sut.getPort());
		
	}
	
}
