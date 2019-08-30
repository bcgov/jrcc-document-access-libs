package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class SftpDocumentInputTests {

    private SftpDocumentInput sut;

    @Mock
    private DocumentReadyHandler documentReadyHandlerMock;

    @Mock
    private DocumentReadyHandler documentReadyHandlerMockException;

    @Mock
    private Message message;

    @Mock
    private Message messageException;

    @Mock
    private TransactionInfo transactionInfoMock;


    @Before
    public void init() throws DocumentMessageException {

        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(documentReadyHandlerMock).handle("message", transactionInfoMock);
        ClassLoader classLoader = getClass().getClassLoader();
        Mockito.when(message.getPayload()).thenReturn(new ByteArrayInputStream("message".getBytes()));

        MessageHeaders messageHeaders = Mockito.mock(MessageHeaders.class);
        Mockito.when(messageHeaders.get("file_remoteFile")).thenReturn("filename.txt");
        Mockito.when(message.getHeaders()).thenReturn(messageHeaders);

        Mockito.when(messageException.getPayload()).thenReturn(new ByteArrayInputStream("contents".getBytes()));
        Map exceptionMap = Mockito.mock(Map.class);
        MessageHeaders exceptionheaders = new MessageHeaders(exceptionMap);
        Mockito.when(messageException.getHeaders()).thenReturn(exceptionheaders);
        sut = new SftpDocumentInput(documentReadyHandlerMock);
    }

    @Test(expected = Test.None.class )
    public void should_handle_incoming_document() throws DocumentMessageException {

        sut.handleMessage(message);

    }

    @Test(expected = IllegalArgumentException.class)
    public void with_message_null_throw_a_IllegalArgumentException() throws DocumentMessageException {

        SftpDocumentInput sftpDocumentInput = new SftpDocumentInput(documentReadyHandlerMock);
        sftpDocumentInput.handleMessage(null);
    }

    @Test(expected = DocumentMessageException.class)
    public void with_filename_null_in_messageHeaders_throw_a_DocumentMessageException() throws DocumentMessageException {
        SftpDocumentInput sftpDocumentInput = new SftpDocumentInput(documentReadyHandlerMock);
        sftpDocumentInput.handleMessage(messageException);
    }
}
