package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

/**
 * The RabbitMqParams holds constants for rabbitmq configuration
 * @author alexjoybc
 * @since 0.3.0
 *
 */
public class RabbitMqParam {

	
	
	public static final String DOCUMENT_READY_TOPIC = "document.ready";
	
	
	public static final String DOCUMENT_READY_BASE_FORMAT = "{0}.{1, number,#}.x{2, number,#}";
	public static final String DOCUMENT_READY_Q_FORMAT = DOCUMENT_READY_BASE_FORMAT + ".q";
	public static final String DOCUMENT_READY_DLQ_FORMAT = DOCUMENT_READY_BASE_FORMAT + ".dlq";
	public static final String DOCUMENT_READY_DLX_FORMAT = DOCUMENT_READY_BASE_FORMAT + ".dlx";
	
	public static final String X_DEAD_LETTER_EXCHANGE_ARG = "x-dead-letter-exchange";
	public static final String X_DEAD_LETTER_ROUTING_KEY_ARG = "x-dead-letter-routing-key";
	public static final String X_MESSAGE_TTL_ARG = "x-message-ttl";
	public static final String X_EXPIRES_ARG = "x-expires";
	
	public static final String X_DEAD_LETTER_ROUTING_KEY = "message-ready-dead";
	
}
