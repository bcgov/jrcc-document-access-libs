package ca.bc.gov.open.jrccaccess.libs;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentTypeTests {

	
	@Test
	public void with_valid_input_should_create_valid_document_info() {
		
		DocumentInfo sut = new DocumentInfo("my-type");
		
		assertEquals("my-type", sut.getType());
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_null_string_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		DocumentInfo sut = new DocumentInfo(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_emtpty_string_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		DocumentInfo sut = new DocumentInfo("");
	}
	
}
