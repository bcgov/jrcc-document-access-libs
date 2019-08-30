package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;


import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;


/**
 * The RabbitMqAutoConfiguration configures rabbitMq plugin
 * @author alexjoybc
 * @since 0.2.0
 */
@Configuration
@EnableConfigurationProperties(RabbitMqOutputProperties.class)
@ComponentScan
@ConditionalOnExpression("'${bcgov.access.input.plugin}' == 'rabbitmq' || '${bcgov.access.output.plugin}' == 'rabbitmq'")
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
        return new Jackson2JsonMessageConverter(objectMapper);
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
	
	
	
	
}
