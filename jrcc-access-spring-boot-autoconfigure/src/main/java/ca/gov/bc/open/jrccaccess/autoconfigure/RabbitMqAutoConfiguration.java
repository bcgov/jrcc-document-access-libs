package ca.gov.bc.open.jrccaccess.autoconfigure;


import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(AccessProperties.class)
@ComponentScan("ca.gov.bc.open.jrccaccess.autoconfigure.services")
public class RabbitMqAutoConfiguration {

	
	@Bean
	@ConditionalOnMissingBean(ConnectionFactory.class) 
	public ConnectionFactory connectionFactory(RabbitProperties rabbitProperties){
		
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitProperties.getHost(), rabbitProperties.getPort());
		
		connectionFactory.setUsername(rabbitProperties.getUsername());
		connectionFactory.setPassword(rabbitProperties.getPassword());
		
		return connectionFactory;
	}
	
	
	@Bean
	public TopicExchange documentReadyTopic() {
		return new TopicExchange(AccessConfigParam.DOCUMENT_READY_TOPIC, true, false);
	}
	
	@Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
	
	
}
