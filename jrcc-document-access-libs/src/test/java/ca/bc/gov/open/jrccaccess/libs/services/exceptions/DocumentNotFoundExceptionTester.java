package ca.bc.gov.open.jrccaccess.libs.services.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentNotFoundExceptionTester {


    @Test
    public void constructor_with_message_should_create_a_new_exception() {

        DocumentNotFoundException sut = new DocumentNotFoundException("not found");

        assertEquals("not found", sut.getMessage());

    }

    @Test
    public void constructor_with_message_and_cause_should_create_a_new_exception(){

        DocumentNotFoundException sut = new DocumentNotFoundException("not found", new RuntimeException());

        assertEquals("not found", sut.getMessage());
        assertEquals(RuntimeException.class, sut.getCause().getClass());

    }


}
