package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import java.text.MessageFormat;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.gov.bc.open.jrccaccess.autoconfigure.AccessProperties;

/**
 * RabbitMq input plugin configuration
 * @author alexjoybc
 * @since 0.4.0
 *
 */
@Configuration
@ConditionalOnProperty(name="bcgov.access.input.plugin", havingValue = "rabbitmq")
@EnableConfigurationProperties(RabbitMqInputProperties.class)
public class InputConfiguration {
	

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory)
    {
        return new RabbitAdmin(connectionFactory);
    }
	
	
	/**
	 * The main queue to listen to
	 * @param accessProperties
	 * @return
	 */
	@Bean
	public Queue documentReadyQueue(AccessProperties accessProperties) {
		Queue queue = QueueBuilder
				.durable(MessageFormat.format("{0}{1}", accessProperties.getInput().getDocumentType(), RabbitMqParam.DOCUMENT_READY_EXTENSION))
				.withArgument(RabbitMqParam.X_DEAD_LETTER_EXCHANGE_ARG, RabbitMqParam.DOCUMENT_READY_DLX)
				.build();
		return queue;
	}
	
	/**
	 * The shared dead letter queue
	 * @return
	 */
	@Bean 
	public Queue documentReadyDeadLetterQueue(AccessProperties accessProperties, RabbitMqInputProperties properties) {
		Queue queue = QueueBuilder
				.durable(RabbitMqParam.DOCUMENT_READY_DLQ)
				.withArgument(RabbitMqParam.X_DEAD_LETTER_EXCHANGE_ARG, RabbitMqParam.DOCUMENT_READY_TOPIC)
				.withArgument(RabbitMqParam.X_MESSAGE_TTL_ARG, properties.getRetryDelay())
				.build();
		return queue;
	}
	
	/**
	 * The shared dead letter topic exchange.
	 * @return
	 */
	@Bean
	public TopicExchange dlxDocumentReadyExchange() {
		return new TopicExchange(RabbitMqParam.DOCUMENT_READY_DLX, true, false);
	}
	
	@Bean
    public Binding binding(@Qualifier("documentReadyTopic")TopicExchange exchange, AccessProperties accessProperties) {
        return BindingBuilder.bind(documentReadyQueue(accessProperties))
        		.to(exchange)
        		.with(accessProperties.getInput().getDocumentType());
    }
	
	@Bean
    public Binding dlqBinding(
    		ConnectionFactory connectionFactory,
    		AccessProperties accessProperties,
    		RabbitMqInputProperties rabbitMqInputProperties) {
        return BindingBuilder
        		.bind(documentReadyDeadLetterQueue(accessProperties, rabbitMqInputProperties))
        		.to(dlxDocumentReadyExchange())
        		.with("#");
    }
	
	/**
	 * Provides as default factory for RabbitListeners
	 * @param connectionFactory
	 * @param messageConverter
	 * @return
	 */
	@Bean
	 public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
			 ConnectionFactory connectionFactory, 
			 @Qualifier("jsonMessageConverter")MessageConverter messageConverter) {
	     SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
	     factory.setConnectionFactory(connectionFactory);
	     factory.setMessageConverter(messageConverter);
	     return factory; 
	 }

	
	
}
