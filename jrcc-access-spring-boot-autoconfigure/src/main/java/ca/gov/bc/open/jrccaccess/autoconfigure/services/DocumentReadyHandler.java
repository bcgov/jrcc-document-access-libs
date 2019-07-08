package ca.gov.bc.open.jrccaccess.autoconfigure.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import ca.gov.bc.open.jrccaccess.libs.DocumentOutput;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;

/**
 * A handler wich in invoked to process incoming documents
 * 
 * @author alexjoybc
 * @since 0.2.0
 *
 */
@Component
public class DocumentReadyHandler {

	private Logger logger = LoggerFactory.getLogger(DocumentReadyHandler.class);

	private DocumentOutput documentOutput;

	public DocumentReadyHandler(DocumentOutput documentOutput) {
		this.documentOutput = documentOutput;
	}

	/**
	 * Handles a give document as inputStream and call the document output
	 * 
	 * @param inputStream
	 * @param sender
	 */
	public void Handle(InputStream inputStream, String sender) throws IOException {

		logger.debug("New document in {}", this.getClass().getName());

		logger.debug("Attempting to create a new transaction");
		TransactionInfo transactionInfo = new TransactionInfo("filename.txt", sender, LocalDateTime.now());

		String content = getContent(inputStream);

		this.documentOutput.send(content, transactionInfo);

	}

	private String getContent(InputStream inputStream) throws IOException {

		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
		}

		return stringBuilder.toString();
	}

}
