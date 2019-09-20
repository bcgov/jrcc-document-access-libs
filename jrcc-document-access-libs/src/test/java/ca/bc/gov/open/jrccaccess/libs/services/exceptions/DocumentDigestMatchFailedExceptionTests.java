package ca.bc.gov.open.jrccaccess.libs.services.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentDigestMatchFailedExceptionTests {


    @Test
    public void constructor_with_message_should_create_a_new_exception() {

        DocumentDigestMatchFailedException sut = new DocumentDigestMatchFailedException("match failed");

        assertEquals("match failed", sut.getMessage());

    }

    @Test
    public void constructor_with_message_and_cause_should_create_a_new_exception(){

        DocumentDigestMatchFailedException sut = new DocumentDigestMatchFailedException("match failed", new RuntimeException());

        assertEquals("match failed", sut.getMessage());
        assertEquals(RuntimeException.class, sut.getCause().getClass());

    }


}
