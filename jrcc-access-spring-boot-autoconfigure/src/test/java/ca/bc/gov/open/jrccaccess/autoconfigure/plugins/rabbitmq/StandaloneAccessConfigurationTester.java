package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AccessApplication.class,
        properties = {
        		"bcgov.access.input.plugin=http",
        		"bcgov.access.output.plugin=rabbitmq"
        })
@ContextConfiguration
public class StandaloneAccessConfigurationTester {

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;

	@Autowired
	private CacheManager cacheManager;

	@Test
	public void with_default_config_should_return_a_valid_stringRedisTemplate() {

		assertNotNull(cacheManager);
	}

	@Test
	public void with_default_config_should_return_a_valid_jedisConnectionFactory() {

		int expectedPort = 6379;

		assertEquals("localhost", this.jedisConnectionFactory.getHostName());

		assertEquals(expectedPort, this.jedisConnectionFactory.getPort());
	}

}
