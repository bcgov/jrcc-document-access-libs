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
@SpringBootTest(
        classes = AccessApplication.class,
        properties = {
        		"bcgov.access.redis.mode=cluster",
        		"bcgov.access.redis.cluster-host-and-port=127.0.0.1:5000,127.0.0.1:5001"
        })
@ContextConfiguration
public class ClusterAccessConfigurationTester {

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Test
	public void with_default_config_should_return_a_valid_stringRedisTemplate() {

		assertEquals(2, ((JedisConnectionFactory)this
				.stringRedisTemplate
				.getConnectionFactory())
				.getClusterConfiguration()
				.getClusterNodes().size());	
		
		

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
