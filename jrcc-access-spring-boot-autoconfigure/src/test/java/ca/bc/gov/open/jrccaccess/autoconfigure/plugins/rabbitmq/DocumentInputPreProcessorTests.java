package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DocumentInputPreProcessorTests {

	
	@Mock
	private RabbitMqInputProperties rabbitMqInputPropertiesMock;
	
	@Mock
	private Message messageMock;
	
	@Mock
	private MessageProperties messageProperties;
	
	@Mock
	List<Map<String, ?>> xDeathCollection;
	
	@Mock
	Map<String, ?> xDeathMock;
	
	private DocumentInputPreProcessor sut;
	
	@Before
	public void init() {
		
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(rabbitMqInputPropertiesMock.getRetryCount()).thenReturn(5);
		
		
		
		sut = new DocumentInputPreProcessor(rabbitMqInputPropertiesMock);
		
	}
	
	@Test
	public void with_no_xdeath_headers_and_no_properties_should_return_the_message() {
		
		
		Message expected = new Message("test".getBytes(), null);
		
		Message actual = sut.postProcessMessage(expected);
		
		assertEquals(expected, actual);

	}
	
	
	@Test
	public void with_no_xdeath_headers_should_return_the_message() {
		
	
		MessageProperties props = new MessageProperties();
		props.setAppId("test");
		
		Message expected = new Message("test".getBytes(), props);
		
		Message actual = sut.postProcessMessage(expected);
		
		assertEquals(expected, actual);

	}
	
	@Test
	public void with_xdeath_lower_than_limit_headers_and_xDeath_not_null_should_return_the_message() {
		
		Mockito.doReturn(4L).when(xDeathMock).get("count");
		Mockito.doReturn(xDeathMock).when(xDeathCollection).get(0);
		Mockito.when(xDeathCollection.isEmpty()).thenReturn(false);
		Mockito.when(messageProperties.getXDeathHeader()).thenReturn(xDeathCollection);
		Mockito.when(messageMock.getMessageProperties()).thenReturn(messageProperties);
		Mockito.when(messageMock.getBody()).thenReturn("test".getBytes());
		
		Message actual = sut.postProcessMessage(messageMock);
		
		assertEquals("test", new String(actual.getBody()));

	}

	@Test
	public void with_xDeath_collection_empty_should_return_the_message() {

		Mockito.when(xDeathCollection.isEmpty()).thenReturn(true);
		Mockito.when(messageProperties.getXDeathHeader()).thenReturn(xDeathCollection);
		Mockito.when(messageMock.getMessageProperties()).thenReturn(messageProperties);
		Mockito.when(messageMock.getBody()).thenReturn("test".getBytes());

		Message actual = sut.postProcessMessage(messageMock);

		assertEquals("test", new String(actual.getBody()));
	}

	@Test
	public void with_xDeath_collection_null_should_return_the_message() {

		xDeathCollection = null;
		Mockito.when(messageProperties.getXDeathHeader()).thenReturn(xDeathCollection);
		Mockito.when(messageMock.getMessageProperties()).thenReturn(messageProperties);
		Mockito.when(messageMock.getBody()).thenReturn("test".getBytes());

		Message actual = sut.postProcessMessage(messageMock);

		assertEquals("test", new String(actual.getBody()));
	}

	@Test
	public void with_xdeath_null_should_return_the_message() {

		Mockito.doReturn(null).when(xDeathCollection).get(0);
		Mockito.when(xDeathCollection.isEmpty()).thenReturn(false);
		Mockito.when(messageProperties.getXDeathHeader()).thenReturn(xDeathCollection);
		Mockito.when(messageMock.getMessageProperties()).thenReturn(messageProperties);
		Mockito.when(messageMock.getBody()).thenReturn("test".getBytes());

		Message actual = sut.postProcessMessage(messageMock);

		assertEquals("test", new String(actual.getBody()));
	}
	
	
	@Test(expected = ImmediateAcknowledgeAmqpException.class)
	public void with_xdeath_higher_than_limit_headers_should_throw_an_ImmediateAcknowledgeAmqpException() {
		
		Mockito.doReturn(10L).when(xDeathMock).get("count");
		Mockito.doReturn(xDeathMock).when(xDeathCollection).get(0);
		Mockito.when(xDeathCollection.isEmpty()).thenReturn(false);
		Mockito.when(messageProperties.getXDeathHeader()).thenReturn(xDeathCollection);
		Mockito.when(messageMock.getMessageProperties()).thenReturn(messageProperties);
		Mockito.when(messageMock.getBody()).thenReturn("test".getBytes());
		
		
		@SuppressWarnings("unused")
		Message actual = sut.postProcessMessage(messageMock);
	}
	
	
}
