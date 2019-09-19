package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import ca.bc.gov.open.jrccaccess.autoconfigure.common.Constants;
import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;
import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentFilenameMissingException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

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
    private PluginConfig inputConfig;

    public SftpDocumentInput(DocumentReadyHandler documentReadyHandler, @Qualifier("inputConfig") PluginConfig inputConfig) {
        this.documentReadyHandler = documentReadyHandler;
        this.inputConfig = inputConfig;
    }


    public void handleMessage(Message<InputStream> message) throws DocumentMessageException {

        if(message == null) throw new IllegalArgumentException("Message is required.");

        if (inputConfig.getSender().equals(Constants.UNKNOWN_SENDER)) {
            logger.warn("Sender not specified in application.yml, using default value of unknown.");
        }

        try {
            logger.debug("Attempting to read downloaded file.");
            String content = getContent(message);
            logger.info("Successfully read downloaded file.");
            logger.debug("Attempting to get file name");
            String fileName = getFilename(message);
            logger.info("Successfully get file name.");
            MDC.put(Constants.MDC_KEY_FILENAME, fileName);
            TransactionInfo transactionInfo = new TransactionInfo(fileName, inputConfig.getSender(), LocalDateTime.now());
            logger.debug("Attempting to handler document content");
            this.documentReadyHandler.handle(content, transactionInfo);
            logger.info("successfully handled incoming document.");
            MDC.clear();
        } catch (IOException e) {
            String logMsg = MessageFormat.format("Sftp Input Plugin error while reading the file.{0},{1}.",e.getMessage(),e.getCause());
            logger.error(logMsg);
            MDC.clear();
            throw new DocumentMessageException(logMsg);
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
