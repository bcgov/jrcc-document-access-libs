package ca.bc.gov.open.jrccaccess.libs.services.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentFilenameMissingExceptionTests {


    @Test
    public void constructor_with_message_should_create_a_new_exception() {

        DocumentFilenameMissingException sut = new DocumentFilenameMissingException("filename missing");

        assertEquals("filename missing", sut.getMessage());

    }

    @Test
    public void constructor_with_message_and_cause_should_create_a_new_exception(){

        DocumentFilenameMissingException sut = new DocumentFilenameMissingException("filename missing", new RuntimeException());

        assertEquals("filename missing", sut.getMessage());
        assertEquals(RuntimeException.class, sut.getCause().getClass());

    }


}
