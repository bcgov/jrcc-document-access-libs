package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.console;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConsolePropertiesTester {

	@Test
	public void with_valid_format_should_return_format() {
		
		ConsoleOutputProperties sut = new ConsoleOutputProperties();
		
		
		sut.setFormat("xml");
		
		assertEquals("xml", sut.getFormat());
		
	}
	
}
