package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.rabbitmq;

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
	public void with_valid_ttl_should_set_ttl() {
		String delayString = "2";

		RabbitMqInputProperties sut = new RabbitMqInputProperties();

		sut.setRetryDelay(delayString);

		assertEquals(delayString, sut.getRetryDelay().toString());
	}
	
	
	@Test
	public void with_ttl_out_of_range_down_should_throw_validation_exception() {

		String delayString = "-10";

		RabbitMqInputProperties sut = new RabbitMqInputProperties();

		sut.setRetryDelay(delayString);
		
		Set<ConstraintViolation<RabbitMqInputProperties>> constraintViolations = validator.validate(sut);

		assertEquals(1, constraintViolations.size());

		assertEquals("must be greater than or equal to 0", constraintViolations.iterator().next().getMessage());
	}

}
