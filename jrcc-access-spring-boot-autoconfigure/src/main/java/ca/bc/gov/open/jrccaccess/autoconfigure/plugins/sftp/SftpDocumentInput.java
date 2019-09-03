package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentFilenameMissingException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.websocket.MessageHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;

/**
 * The Sftp document input reads file stream from SFTP server
 * @author 177226
 */
@Component
public class SftpDocumentInput implements MessageHandler {

    private Logger logger = LoggerFactory.getLogger(SftpDocumentInput.class);

    private DocumentReadyHandler documentReadyHandler;

    public SftpDocumentInput(DocumentReadyHandler documentReadyHandler) {
        this.documentReadyHandler = documentReadyHandler;
    }


    public void handleMessage(Message<InputStream> message) throws DocumentMessageException {

        if(message == null) throw new IllegalArgumentException("Message is required.");

        try {
            logger.debug("Attempting to read downloaded file.");
            String content = getContent(message);
            logger.info("Successfully read downloaded file.");
            logger.debug("Attempting to get file name");
            String fileName = getFilename(message);
            logger.info("Successfully get file name.");
            TransactionInfo transactionInfo = new TransactionInfo(fileName,"sftp", LocalDateTime.now());
            logger.debug("Attempting to handler document content");
            this.documentReadyHandler.handle(content, transactionInfo);
            logger.info("successfully handled incoming document.");
        } catch (IOException e) {
            throw new DocumentMessageException(MessageFormat.format("Sftp Input Plugin error while reading the file.{0},{1}.",e.getMessage(),e.getCause()));
        }

    }


    private String getContent(Message<InputStream> message) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(message.getPayload(), StandardCharsets.UTF_8))) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        return stringBuilder.toString();
    }

    private String getFilename(Message<InputStream> message) throws DocumentFilenameMissingException{
        MessageHeaders messageHeaders = message.getHeaders();
        Object filenameObj = messageHeaders.get("file_remoteFile");
        if (!(filenameObj instanceof String )){
            throw new DocumentFilenameMissingException("corrupted SFTP header. Filename is required.");
        }
        return filenameObj.toString();
    }

}
