package ca.gov.bc.open.jrccaccess.autoconfigure;

import javax.naming.OperationNotSupportedException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@ComponentScan("ca.gov.bc.open.jrccaccess.autoconfigure.services")
public class AccessAutoConfiguration {

	/**
	 * Configure the JedisConnectionFactory
	 * @param properties The redis properties
	 * @return a JedisConnectionFactory
	 * @throws OperationNotSupportedException if mode is set to sentinel, this mode is not supported yet. 
	 */
	@Bean
	@ConditionalOnMissingBean(JedisConnectionFactory.class)
	public JedisConnectionFactory jedisConnectionFactory(RedisProperties properties) throws OperationNotSupportedException {
		
		switch (properties.getMode()) {
		case CLUSTER:
			RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(properties.getClusterHostAndPort());
			return new JedisConnectionFactory(redisClusterConfiguration);
		case SANTINEL:
			throw new OperationNotSupportedException("Santinel mode is not supported");
		default:
			RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(properties.getHost(), properties.getPort());
			return new JedisConnectionFactory(redisStandaloneConfiguration);
		}
	}
	
	/**
	 * Configure the default StringRedisTempate
	 * @param jedisConnectionFactory
	 * @return a StringRedisTemplate
	 */
	@Bean
	@ConditionalOnMissingBean(StringRedisTemplate.class)
	public StringRedisTemplate stringRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {	
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(jedisConnectionFactory);
		return template;
	}
	
}
