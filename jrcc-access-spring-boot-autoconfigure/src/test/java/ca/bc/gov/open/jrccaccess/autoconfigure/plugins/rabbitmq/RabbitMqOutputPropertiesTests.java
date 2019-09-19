package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.rabbitmq;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RabbitMqOutputPropertiesTests {

	private static Validator validator;

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	
	@Test
	public void with_valid_ttl_should_set_ttl() {
		String ttl = "2";

		RabbitMqOutputProperties sut = new RabbitMqOutputProperties();

		sut.setTtl(ttl);

		assertEquals(ttl, sut.getTtl().toString());
	}

	@Test
	public void with_null_ttl_should_return_default_ttl() {
		String ttl = "1";
		RabbitMqOutputProperties sut = new RabbitMqOutputProperties();
		assertEquals(ttl, sut.getTtl().toString());
	}
	
	@Test
	public void with_ttl_out_of_range_down_should_throw_validation_exception() {

		String ttl = "-10";

		RabbitMqOutputProperties sut = new RabbitMqOutputProperties();

		sut.setTtl(ttl);
		
		Set<ConstraintViolation<RabbitMqOutputProperties>> constraintViolations = validator.validate(sut);

		assertEquals(1, constraintViolations.size());

		assertEquals("must be greater than or equal to 0", constraintViolations.iterator().next().getMessage());
	}

}
