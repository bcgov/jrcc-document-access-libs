package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

/**
 * The RabbitMqParams holds constants for rabbitmq configuration
 * @author alexjoybc
 * @since 0.3.0
 *
 */
public class RabbitMqParam {

	public static final String DOCUMENT_READY_TOPIC = "document.ready";
	public static final String DOCUMENT_READY_EXTENSION = ".q";
	public static final String DOCUMENT_READY_DLQ = DOCUMENT_READY_TOPIC + ".dlq";
	public static final String DOCUMENT_READY_DLX = DOCUMENT_READY_TOPIC + ".dlx";
	
	public static final String X_DEAD_LETTER_EXCHANGE_ARG = "x-dead-letter-exchange";
	public static final String X_DEAD_LETTER_ROUTING_KEY_ARG = "x-dead-letter-routing-key";
	public static final String X_MESSAGE_TTL_ARG = "x-message-ttl";
	
	public static final String X_DEAD_LETTER_ROUTING_KEY = "message-ready-dead";
	
}
