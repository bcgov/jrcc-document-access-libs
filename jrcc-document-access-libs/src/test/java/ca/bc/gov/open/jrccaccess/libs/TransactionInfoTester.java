package ca.bc.gov.open.jrccaccess.libs;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class TransactionInfoTester {

	
	@Test
	public void with_valid_input_should_create_a_transactionInfo() {
		
		
		TransactionInfo sut = new TransactionInfo("myfile.txt", "me", LocalDateTime.of(2019, Month.FEBRUARY, 1, 10,10,10));
		
		assertEquals("myfile.txt", sut.getFileName());
		assertEquals("me", sut.getSender());
		
		assertEquals(2019, sut.getReceivedOn().getYear());
		assertEquals(Month.FEBRUARY, sut.getReceivedOn().getMonth());
		assertEquals(1, sut.getReceivedOn().getDayOfMonth());

	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_null_file_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		TransactionInfo sut = new TransactionInfo(null, "me", LocalDateTime.of(2019, Month.FEBRUARY, 1, 10,10,10));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_empty_file_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		TransactionInfo sut = new TransactionInfo("", "me", LocalDateTime.of(2019, Month.FEBRUARY, 1, 10,10,10));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_null_sender_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		TransactionInfo sut = new TransactionInfo("myfile.txt", null, LocalDateTime.of(2019, Month.FEBRUARY, 1, 10,10,10));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_empty_sender_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		TransactionInfo sut = new TransactionInfo("myfile.txt", "", LocalDateTime.of(2019, Month.FEBRUARY, 1, 10,10,10));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void with_null_receivedon_should_throw_illegalArgumentException() {
		@SuppressWarnings("unused")
		TransactionInfo sut = new TransactionInfo("myfile.txt", "me", null);
	}
	
	@Test
	public void with_valid_input_toString_should_print_valid_message() {
		
		TransactionInfo sut = new TransactionInfo("myfile.txt", "me", LocalDateTime.of(2019, Month.FEBRUARY, 1, 10,10,10));
	
		assertEquals("Transaction sent from [me] on [2019-02-01T10:10:10], fileName [myfile.txt]", sut.toString());
		
	}
	
	
	
	
}
