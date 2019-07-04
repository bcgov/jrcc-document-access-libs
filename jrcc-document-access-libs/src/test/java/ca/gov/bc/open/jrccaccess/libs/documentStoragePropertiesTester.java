package ca.gov.bc.open.jrccaccess.libs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class documentStoragePropertiesTester {

	
	@Test
	public void with_valid_input_should_return_docuement_storage_properties() {
		
		DocumentStorageProperties sut = new DocumentStorageProperties("my key", "A123B1");	
		assertEquals("my key", sut.getKey());
		assertEquals("A123B1", sut.getMD5());
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_empty_key_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		DocumentStorageProperties sut = new DocumentStorageProperties("", "A123B1");		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_null_key_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		DocumentStorageProperties sut = new DocumentStorageProperties(null, "A123B1");		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_empty_MD5_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		DocumentStorageProperties sut = new DocumentStorageProperties("my key", "");		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_null_MD5_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		DocumentStorageProperties sut = new DocumentStorageProperties("my key", null);		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_not_hex_MD5_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		DocumentStorageProperties sut = new DocumentStorageProperties("my key", "not hexadecimal");		
	}
	
	
	
}
