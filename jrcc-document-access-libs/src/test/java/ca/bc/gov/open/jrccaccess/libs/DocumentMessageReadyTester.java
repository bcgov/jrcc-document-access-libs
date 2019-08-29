package ca.bc.gov.open.jrccaccess.libs;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class DocumentMessageReadyTester {

	private static final TransactionInfo TRANSACTION_INFO = new TransactionInfo("myfile.txt", "me", LocalDateTime.of(2019, Month.FEBRUARY, 1, 10,10,10));
	private static final DocumentInfo DOCUMENT_INFO = new DocumentInfo("my-type");
	private static final DocumentStorageProperties DOCUMENT_STORAGE_PROPERTIES = new DocumentStorageProperties("my key", "A123B1");	
	
	@Test
	public void with_valid_imput_should_crate_a_valid_message() {
		
		
		DocumentReadyMessage sut = new DocumentReadyMessage(TRANSACTION_INFO, DOCUMENT_INFO, DOCUMENT_STORAGE_PROPERTIES);
		
		assertEquals("myfile.txt", sut.getTransactionInfo().getFileName());
		assertEquals("me", sut.getTransactionInfo().getSender());

		assertEquals(2019, sut.getTransactionInfo().getReceivedOn().getYear());
		assertEquals(Month.FEBRUARY, sut.getTransactionInfo().getReceivedOn().getMonth());
		assertEquals(1, sut.getTransactionInfo().getReceivedOn().getDayOfMonth());

		
		
		assertEquals("my-type", sut.getDocumentInfo().getType());
		
		
		assertEquals("my key", sut.getDocumentStorageProperties().getKey());
		assertEquals("A123B1", sut.getDocumentStorageProperties().getMD5());
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_null_transactionInfo_should_crate_a_valid_message() {

		@SuppressWarnings("unused")
		DocumentReadyMessage sut = new DocumentReadyMessage(null, DOCUMENT_INFO, DOCUMENT_STORAGE_PROPERTIES);

	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_null_docuementInfo_should_crate_a_valid_message() {

		@SuppressWarnings("unused")
		DocumentReadyMessage sut = new DocumentReadyMessage(TRANSACTION_INFO, null, DOCUMENT_STORAGE_PROPERTIES);

	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_null_documentStorageProperties_should_crate_a_valid_message() {

		@SuppressWarnings("unused")
		DocumentReadyMessage sut = new DocumentReadyMessage(TRANSACTION_INFO, DOCUMENT_INFO, null);

	}
	
	@Test()
	public void with_valid_imput_toString_should_print_valid_message() {

		DocumentReadyMessage sut = new DocumentReadyMessage(TRANSACTION_INFO, DOCUMENT_INFO, DOCUMENT_STORAGE_PROPERTIES);
		assertEquals("Document Message Ready for Transaction with [me] on document type [my-type], stored with key [my key]", sut.toString());

	}
	
	
	
	
}
