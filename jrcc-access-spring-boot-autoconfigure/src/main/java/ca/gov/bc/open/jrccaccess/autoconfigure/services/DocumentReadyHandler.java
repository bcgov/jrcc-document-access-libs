package ca.gov.bc.open.jrccaccess.autoconfigure.services;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ca.gov.bc.open.jrccaccess.libs.DocumentOutput;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;
import ca.gov.bc.open.jrccaccess.libs.services.exceptions.DocumentMessageException;

/**
 * The document ready handler is the global handler for incoming documents
 * 
 * @author alexjoybc
 * @since 0.2.0
 *
 */
@Component
public class DocumentReadyHandler {

	private Logger logger = LoggerFactory.getLogger(DocumentReadyHandler.class);

	private DocumentOutput documentOutput;

	/**
	 * Creates a document ready handler with a given document output
	 * @param documentOutput
	 */
	public DocumentReadyHandler(DocumentOutput documentOutput) {
		this.documentOutput = documentOutput;
	}

	/**
	 * Handles a give document as inputStream and call the document output
	 * 
	 * @param inputStream
	 * @param sender
	 */
	public void handle(String message, String sender) throws DocumentMessageException {

		logger.debug("New document in {}", this.getClass().getName());

		logger.debug("Attempting to create a new transaction");
		TransactionInfo transactionInfo = new TransactionInfo("filename.txt", sender, LocalDateTime.now());
		
		// for each validation
			// if false throw ValidationEx	
		
		this.documentOutput.send(message, transactionInfo);

	}

}
