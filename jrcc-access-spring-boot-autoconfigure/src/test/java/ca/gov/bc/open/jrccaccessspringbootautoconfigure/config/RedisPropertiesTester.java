package ca.gov.bc.open.jrccaccessspringbootautoconfigure.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class RedisPropertiesTester {

	private static Validator validator;

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void with_valid_port_should_set_port() {

		String port = "1234";

		RedisProperties sut = new RedisProperties();

		sut.setPort(port);

		assertEquals(port, sut.getPort().toString());

	}

	@Test(expected = NumberFormatException.class)
	public void with_invalid_port_should_throw_exception() {

		String port = "abcd";
		RedisProperties sut = new RedisProperties();

		sut.setPort(port);

	}

	@Test
	public void with_port_out_of_range_down_should_throw_validation_exception() {

		String port = "1";
		RedisProperties sut = new RedisProperties();

		sut.setPort(port);
		
		Set<ConstraintViolation<RedisProperties>> constraintViolations = validator.validate(sut);

		assertEquals(1, constraintViolations.size());

		assertEquals("must be greater than or equal to 1025", constraintViolations.iterator().next().getMessage());

	}
	
	@Test
	public void with_port_out_of_range_up_should_throw_validation_exception() {

		String port = "10000000";
		RedisProperties sut = new RedisProperties();

		sut.setPort(port);
		
		Set<ConstraintViolation<RedisProperties>> constraintViolations = validator.validate(sut);

		assertEquals(1, constraintViolations.size());

		assertEquals("must be less than or equal to 65536", constraintViolations.iterator().next().getMessage());

	}
	
	@Test
	public void with_no_port_set_should_return_6379() {
		
		RedisProperties sut = new RedisProperties();
		
		Integer expected = 6379;
		
		assertEquals(expected, sut.getPort());

	}
	
	@Test
	public void with_host_should_return_host() {
		
		String host = "my-host";
		
		RedisProperties sut = new RedisProperties();
		
		sut.setHost(host);
		
		assertEquals(host, sut.getHost());
	}
	
	@Test
	public void with_null_host_should_return_locahost() {
		
		
		RedisProperties sut = new RedisProperties();
		
		assertEquals("localhost", sut.getHost());
	}
	
	@Test
	public void with_cluster_mode_should_return_cluster_mode() {
		
		
		RedisProperties sut = new RedisProperties();
		
		sut.setMode("cluster");
		
		assertEquals(RedisMode.CLUSTER, sut.getMode());
		
	}
	
	@Test
	public void with_cluster_mode_not_set_should_return_standalone_mode() {
		
		RedisProperties sut = new RedisProperties();
		
		assertEquals(RedisMode.STANDALONE, sut.getMode());
		
	}
	
	@Test
	public void with_valid_string_collection_should_return_collection() {
		
		RedisProperties sut = new RedisProperties();
		
		sut.setClusterHostAndPort("127.0.0.0:22679,127.0.0.0:22680");
		
		assertEquals(2, sut.getClusterHostAndPort().size());
	}
	
	@Test
	public void with_valid_string_collection_not_set_should_return_null() {
		
		RedisProperties sut = new RedisProperties();
		
		assertNull(sut.getClusterHostAndPort());
	}


}
