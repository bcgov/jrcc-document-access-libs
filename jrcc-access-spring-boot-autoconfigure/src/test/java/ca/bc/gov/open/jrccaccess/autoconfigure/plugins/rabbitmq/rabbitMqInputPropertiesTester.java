package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class rabbitMqInputPropertiesTester {

	private static Validator validator;

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	
	@Test
	public void with_valid_ttl_should_set_RetryDelay() {
		String delayString = "2";

		RabbitMqInputProperties sut = new RabbitMqInputProperties();

		sut.setRetryDelay(delayString);

		assertEquals(delayString, sut.getRetryDelay().toString());
	}
	
	
	@Test
	public void with_RetryDelay_out_of_range_down_should_throw_validation_exception() {

		String delayString = "-10";

		RabbitMqInputProperties sut = new RabbitMqInputProperties();

		sut.setRetryDelay(delayString);
		
		Set<ConstraintViolation<RabbitMqInputProperties>> constraintViolations = validator.validate(sut);

		assertEquals(1, constraintViolations.size());

		assertEquals("must be greater than or equal to 0", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void with_valid_retryCount_should_set_RetryCount() {
		String countString = "2";

		RabbitMqInputProperties sut = new RabbitMqInputProperties();

		sut.setRetryCount(countString);

		assertEquals(countString, sut.getRetryCount().toString());
	}
	
	@Test
	public void with_nullRetryCount_should_return_default_RetryCount() {
		String countString = "0";
		RabbitMqInputProperties sut = new RabbitMqInputProperties();
		assertEquals(countString, sut.getRetryCount().toString());
	}
	
	
	@Test
	public void with_RetryCount_out_of_range_down_should_throw_validation_exception() {

		String countString = "-10";

		RabbitMqInputProperties sut = new RabbitMqInputProperties();

		sut.setRetryCount(countString);
		
		Set<ConstraintViolation<RabbitMqInputProperties>> constraintViolations = validator.validate(sut);

		assertEquals(1, constraintViolations.size());

		assertEquals("must be greater than or equal to 0", constraintViolations.iterator().next().getMessage());
	}

}
