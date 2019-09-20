package ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KnownHostFileNotDefinedExceptionTests {


    @Test
    public void constructor_with_message_should_create_a_new_exception() {

        KnownHostFileNotDefinedException sut = new KnownHostFileNotDefinedException("not defined");

        assertEquals("not defined", sut.getMessage());

    }

}
