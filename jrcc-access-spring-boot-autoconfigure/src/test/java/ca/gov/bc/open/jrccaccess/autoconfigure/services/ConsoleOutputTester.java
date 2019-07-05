package ca.gov.bc.open.jrccaccess.autoconfigure.services;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;

public class ConsoleOutputTester {

	private ConsoleOutput sut;
	
	@Before
	public void Init() {
		sut = new ConsoleOutput();
	}
	
	@Test
	public void send_with_valid_input_should_print_to_console() {
	
		sut.send("my content", new TransactionInfo("my file.txt", "bcgov", LocalDateTime.now()));
		
	}
	
	
}
