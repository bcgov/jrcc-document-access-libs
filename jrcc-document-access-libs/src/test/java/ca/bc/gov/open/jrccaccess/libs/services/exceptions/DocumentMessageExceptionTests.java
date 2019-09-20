package ca.bc.gov.open.jrccaccess.libs.services.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentMessageExceptionTests {


    @Test
    public void constructor_should_create_a_new_exception() {

        DocumentMessageException sut = new DocumentMessageException();

        assertEquals(null, sut.getMessage());

    }

    @Test
    public void constructor_with_message_should_create_a_new_exception() {

        DocumentMessageException sut = new DocumentMessageException("message exception");

        assertEquals("message exception", sut.getMessage());

    }

    @Test
    public void constructor_with_message_and_cause_should_create_a_new_exception(){

        DocumentMessageException sut = new DocumentMessageException("message exception", new RuntimeException());

        assertEquals("message exception", sut.getMessage());
        assertEquals(RuntimeException.class, sut.getCause().getClass());

    }


}
