package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import javax.websocket.MessageHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class SftpDocumentInput implements MessageHandler {

    private Logger logger = LoggerFactory.getLogger(SftpDocumentInput.class);

    private DocumentReadyHandler documentReadyHandler;

    public SftpDocumentInput(DocumentReadyHandler documentReadyHandler) {
        this.documentReadyHandler = documentReadyHandler;
    }


    public void handleMessage(Message<InputStream> message) throws MessagingException, DocumentMessageException {

        if(message == null) throw new IllegalArgumentException("Message is required.");


        try {
            logger.debug("Attempting to read downloaded file.");
            String content = getContent(message);
            logger.info("Successfully red downloaded file.");

            logger.debug("Attempting to handler document content");
            this.documentReadyHandler.handle(content, "unknown");
            logger.info("successfully handled incoming document.");
        } catch (IOException e) {
            logger.error("Sftp Input Plugin error while reading the file.");
           throw new DocumentMessageException("Sftp Input Plugin error while reading the file", e.getCause());
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

}
