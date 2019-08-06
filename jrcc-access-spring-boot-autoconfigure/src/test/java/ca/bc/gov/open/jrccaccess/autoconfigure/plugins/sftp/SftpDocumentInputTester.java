package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class SftpDocumentInputTester {

    private SftpDocumentInput sut;

    @Mock
    private DocumentReadyHandler documentReadyHandlerMock;

    @Mock
    private DocumentReadyHandler documentReadyHandlerMockException;

    @Mock
    private Message message;

    @Mock
    private Message messageException;


    @Before
    public void init() throws DocumentMessageException {

        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(documentReadyHandlerMock).handle(Mockito.anyString(), Mockito.anyString());


        ClassLoader classLoader = getClass().getClassLoader();
        Mockito.when(message.getPayload()).thenReturn(new ByteArrayInputStream("test".getBytes()));

        sut = new SftpDocumentInput(documentReadyHandlerMock);

    }

    @Test
    public void should_handle_incoming_document() throws DocumentMessageException {

        sut.handleMessage(message);

    }

    @Test(expected = IllegalArgumentException.class)
    public void with_message_null_throw_a_IllegalArgumentException() throws DocumentMessageException {

        SftpDocumentInput sftpDocumentInput = new SftpDocumentInput(documentReadyHandlerMock);
        sftpDocumentInput.handleMessage(null);
    }


}
