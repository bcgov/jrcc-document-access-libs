package ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KnownHostFileNotFoundExceptionTests {


    @Test
    public void constructor_with_message_should_create_a_new_exception() {

        KnownHostFileNotFoundException sut = new KnownHostFileNotFoundException("not found");

        assertEquals("not found", sut.getMessage());

    }

}
