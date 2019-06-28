package ca.gov.bc.open.jrccaccessspringbootautoconfigure.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccessApplication.class)
@ContextConfiguration
public class StandaloneAccessConfigurationTester {

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Test
	public void with_default_config_should_return_a_valid_stringRedisTemplate() {

		int expectedPort = 6379;

		assertEquals("localhost", ((JedisConnectionFactory)this
				.stringRedisTemplate
				.getConnectionFactory()).getHostName());
		
		assertEquals(expectedPort, ((JedisConnectionFactory)this
				.stringRedisTemplate
				.getConnectionFactory()).getPort());			
	}
	
	@Test
	public void with_default_config_should_return_a_valid_jedisConnectionFactory() {

		int expectedPort = 6379;

		assertEquals("localhost", this.jedisConnectionFactory.getHostName());
		
		assertEquals(expectedPort, this.jedisConnectionFactory.getPort());			
	}
	
}
