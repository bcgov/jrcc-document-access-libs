package ca.gov.bc.open.jrccaccess.autoconfigure.rabbitmq;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccessApplication.class)
@ContextConfiguration
public class ConnectionFactoryTester {

	@Autowired
	private ConnectionFactory sut;
	
	@Test
	public void should_return_a_valid_connection_factory() {
		
		
		assertEquals("localhost", sut.getHost());
		assertEquals(5672, sut.getPort());
		
	}
	
}
