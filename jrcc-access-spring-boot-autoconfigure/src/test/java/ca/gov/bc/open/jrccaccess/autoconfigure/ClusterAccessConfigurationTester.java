package ca.gov.bc.open.jrccaccess.autoconfigure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AccessApplication.class,
        properties = {
        		"spring.redis.cluster.nodes=127.0.0.1:5000,127.0.0.1:5001",
        		"bcgov.access.input=http",
        		"bcgov.access.output=rabbitmq"
        })
@ContextConfiguration
public class ClusterAccessConfigurationTester {

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

		assertEquals(2, this.jedisConnectionFactory
				.getClusterConfiguration()
				.getClusterNodes().size());		
		
		this.jedisConnectionFactory
				.getClusterConfiguration()
				.getClusterNodes()
				.forEach(redisNode -> assertEquals("127.0.0.1",redisNode.getHost()));		
	}
	
}
