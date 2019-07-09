package ca.gov.bc.open.jrccaccess.autoconfigure;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq.RabbitMqOutputProperties;

/**
 * The AccessAutoConfiguration is the default configuration for the access library
 * @author alexjoybc
 * @since 0.0.1
 */
@Configuration
@EnableConfigurationProperties({ RabbitMqOutputProperties.class, AccessProperties.class })
@ComponentScan("ca.gov.bc.open.jrccaccess.autoconfigure.services")
public class AccessAutoConfiguration {

	private AccessProperties accessProperties;
	private Logger logger = LoggerFactory.getLogger(AccessAutoConfiguration.class);
	
	
	public AccessAutoConfiguration(AccessProperties accessProperties) {
		this.accessProperties = accessProperties;
		logger.info("Bootstraping Access Library", accessProperties.getOutput().getPlugin());
		logger.info("Output plugin: {}", accessProperties.getOutput().getPlugin());
	}
	
}
