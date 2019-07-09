package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.console;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConsolePropertiesTester {

	@Test
	public void with_valid_format_should_return_format() {
		
		ConsoleOutputProperties sut = new ConsoleOutputProperties();
		
		
		sut.setFormat("xml");
		
		assertEquals("xml", sut.getFormat());
		
	}
	
}
