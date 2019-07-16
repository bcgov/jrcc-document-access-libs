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
 * 
 * @author alexjoybc
 * @since 0.4.0
 *
 */
@Configuration
@ConditionalOnProperty(name = "bcgov.access.input.plugin", havingValue = "rabbitmq")
@EnableConfigurationProperties(RabbitMqInputProperties.class)
public class InputConfiguration {

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	/**
	 * The main queue to listen to
	 * 
	 * @param accessProperties
	 * @return
	 */
	@Bean
	public Queue documentReadyQueue(AccessProperties accessProperties, RabbitMqInputProperties properties) {
		Queue queue = QueueBuilder
				.durable(getNameSpace(RabbitMqParam.DOCUMENT_READY_Q_FORMAT, accessProperties, properties))
				.withArgument(RabbitMqParam.X_DEAD_LETTER_EXCHANGE_ARG,
						dlxDocumentReadyExchange(accessProperties, properties).getName())
				.build();
		return queue;
	}

	/**
	 * The document ready binding, binds the document ready queue to the document exchange.
	 * @param exchange
	 * @param accessProperties
	 * @param properties
	 * @return
	 */
	@Bean
	public Binding documentReadyBinding(@Qualifier("documentReadyTopic") TopicExchange exchange,
			AccessProperties accessProperties, RabbitMqInputProperties properties) {
		return BindingBuilder.bind(documentReadyQueue(accessProperties, properties)).to(exchange)
				.with(accessProperties.getInput().getDocumentType());
	}

	/**
	 * The shared dead letter queue
	 * 
	 * @return
	 */
	@Bean
	public Queue documentReadyDeadLetterQueue(AccessProperties accessProperties, RabbitMqInputProperties properties) {
		Queue queue = QueueBuilder
				.durable(getNameSpace(RabbitMqParam.DOCUMENT_READY_DLQ_FORMAT, accessProperties, properties))
				.withArgument(RabbitMqParam.X_DEAD_LETTER_EXCHANGE_ARG, RabbitMqParam.DOCUMENT_READY_TOPIC)
				.withArgument(RabbitMqParam.X_MESSAGE_TTL_ARG, properties.getRetryDelay()).build();
		return queue;
	}

	/**
	 * Sets the document ready dead letter exchange
	 * 
	 * @return
	 */
	@Bean
	public TopicExchange dlxDocumentReadyExchange(AccessProperties accessProperties,
			RabbitMqInputProperties properties) {
		return new TopicExchange(getNameSpace(RabbitMqParam.DOCUMENT_READY_DLQ_FORMAT, accessProperties, properties),
				true, false);
	}
	
	
	/**
	 * Binds the dead letter queue to the dead letter exchange.
	 * @param connectionFactory
	 * @param accessProperties
	 * @param rabbitMqInputProperties
	 * @return
	 */
	@Bean
	public Binding documentReadyDeadLetterQueueBinding(ConnectionFactory connectionFactory,
			AccessProperties accessProperties, RabbitMqInputProperties rabbitMqInputProperties) {
		return BindingBuilder.bind(documentReadyDeadLetterQueue(accessProperties, rabbitMqInputProperties))
				.to(dlxDocumentReadyExchange(accessProperties, rabbitMqInputProperties)).with("#");
	}

	/**
	 * Provides as default factory for RabbitListeners
	 * 
	 * @param connectionFactory
	 * @param messageConverter
	 * @return
	 */
	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
			@Qualifier("jsonMessageConverter") MessageConverter messageConverter, DocumentInputPreProcessor documentInputPreProcessor) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter);
		factory.setAfterReceivePostProcessors(documentInputPreProcessor);
		return factory;
	}

	private String getNameSpace(String pattern, AccessProperties accessProperties, RabbitMqInputProperties properties) {

		return MessageFormat.format(pattern, accessProperties.getInput().getDocumentType(), properties.getRetryDelay(),
				properties.getRetryCount());
		
	}

}
