package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;


import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessProperties;


/**
 * The RabbitMqAutoConfiguration configures rabbitMq plugin
 * @author alexjoybc
 * @since 0.2.0
 */
@Configuration
@EnableConfigurationProperties(RabbitMqOutputProperties.class)
@ComponentScan
public class AutoConfiguration {

	/**
	 * ConnectionFactory Bean
	 * @param rabbitProperties The rabbitMQProperties
	 * @return A Connection Factory
	 */
	@Bean
	@ConditionalOnMissingBean(ConnectionFactory.class) 
	public ConnectionFactory connectionFactory(RabbitProperties rabbitProperties){
		
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitProperties.getHost(), rabbitProperties.getPort());
		
		connectionFactory.setUsername(rabbitProperties.getUsername());
		connectionFactory.setPassword(rabbitProperties.getPassword());
		
		return connectionFactory;
	}
	
	

	/**
	 * TopicExchangeBean
	 * @return The document Ready Topic
	 */
	@Bean
	public TopicExchange documentReadyTopic() {
		return new TopicExchange(RabbitMqParam.DOCUMENT_READY_TOPIC, true, false);
	}
	
	
	@Bean
	@Primary
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {	
		ObjectMapper objectMapper = builder.build();
	    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	    return objectMapper;
	}
	
	/**
	 * MessageConverter bean
	 * @return A JsonMessageConverter
	 */
	@Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper){
        Jackson2JsonMessageConverter converter  = new Jackson2JsonMessageConverter(objectMapper);
		return converter;
    }
	
	/**
	 * RabbitTemplate Bean
	 * @param rabbitProperties
	 * @param accessProperties
	 * @return The documentReadyTemplate for publishing document to topic ready exchange
	 */
	@Bean
	@ConditionalOnProperty(name="bcgov.access.output.plugin", havingValue = "rabbitmq")
	public RabbitTemplate documentReadyTopicTemplate(RabbitMqOutputProperties rabbitMqOutputProperties, RabbitProperties rabbitProperties, AccessProperties accessProperties, ObjectMapper objectMapper) {
		
		RabbitTemplate rabbitTemplate = new RabbitTemplate(this.connectionFactory(rabbitProperties));
		rabbitTemplate.setExchange(this.documentReadyTopic().getName());
		rabbitTemplate.setRoutingKey(accessProperties.getOutput().getDocumentType());
		rabbitTemplate.setMessageConverter(this.jsonMessageConverter(objectMapper));
		return rabbitTemplate;
		
	}
	
	
	/**
	 * Configure the JedisConnectionFactory
	 * @param properties The redis properties
	 * @return a JedisConnectionFactory
	 * @throws OperationNotSupportedException if mode is set to sentinel, this mode is not supported yet. 
	 */
	@Bean
	@ConditionalOnMissingBean(JedisConnectionFactory.class)
	public JedisConnectionFactory jedisConnectionFactory(RedisProperties properties) {
		
		
		if(properties.getCluster() != null) {
			RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(properties.getCluster().getNodes());
			redisClusterConfiguration.setPassword(properties.getPassword());
			
			if(properties.getCluster().getMaxRedirects() != null)
				redisClusterConfiguration.setMaxRedirects(properties.getCluster().getMaxRedirects());
			
			return new JedisConnectionFactory(redisClusterConfiguration);
		}
		
		if(properties.getSentinel() != null) {
			RedisSentinelConfiguration redisSantinelConfiguration = new RedisSentinelConfiguration();
			redisSantinelConfiguration.setMaster(properties.getSentinel().getMaster());
			redisSantinelConfiguration.setSentinels(createSentinels(properties.getSentinel()));
			redisSantinelConfiguration.setPassword(properties.getPassword());
			return new JedisConnectionFactory(redisSantinelConfiguration);
		}
		
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(properties.getHost());
		redisStandaloneConfiguration.setPort(properties.getPort());
		redisStandaloneConfiguration.setPassword(properties.getPassword());
		return new JedisConnectionFactory(redisStandaloneConfiguration);
	}
	
	
	private List<RedisNode> createSentinels(Sentinel sentinel) {
        List<RedisNode> nodes = new ArrayList<RedisNode>();
        for (String node : sentinel.getNodes()) {
           try {
              String[] parts = node.split(":");
              nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
           }
           catch (RuntimeException ex) {
              throw new IllegalStateException(
                    "Invalid redis sentinel " + "property '" + node + "'", ex);
           }
        }
        return nodes;
     }
	
	/**
	 * Configures the cache manager
	 * @param jedisConnectionFactory A jedisConnectionFactory
	 * @param accessProperties Custom access properties
	 * @return
	 */
	@Bean(name = "Document")
	@ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager(JedisConnectionFactory jedisConnectionFactory, RabbitMqOutputProperties rabbitMqOutputProperties) {

		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
	            .disableCachingNullValues()
	            .entryTtl(Duration.ofHours(rabbitMqOutputProperties.getTtl()));
	    redisCacheConfiguration.usePrefix();

	   return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
	                    .cacheDefaults(redisCacheConfiguration).build();
    }
	
}
