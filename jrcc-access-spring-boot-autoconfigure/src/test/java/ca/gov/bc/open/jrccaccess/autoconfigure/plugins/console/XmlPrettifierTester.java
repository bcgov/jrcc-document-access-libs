package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.console;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class XmlPrettifierTester {

	
	private Prettifier sut;
	
	@Before
	public void init() {
		sut = new XmlPrettifier();
	}
	
	@Test
	public void with_valid_input_should_prettify() {
		
		String validInput = "<?xml version=\"1.0\"?><catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developer's Guide</title><genre>Computer</genre><price>44.95</price><publish_date>2000-10-01</publish_date><description>An in-depth look at creating applications with XML.</description></book></catalog>";
		
		String result = sut.prettify(validInput);
		
		assertTrue(validInput.length() < result.length());
		
	}
	
	@Test
	public void with_invalid_input_should_return_original_value() {
		
		String invalidInput = "not an xml string";
		String result = sut.prettify(invalidInput);
		
		assertEquals(invalidInput, result);
		
	}
	
	
}
