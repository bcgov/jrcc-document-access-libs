package ca.bc.gov.open.jrccaccess.autoconfigure.services;

import ca.bc.gov.open.jrccaccess.libs.DocumentOutput;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.processing.DocumentProcessor;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

	private Optional<DocumentProcessor> processor;

	/**
	 * Creates a document ready handler with a given document output
	 * 
	 * @param documentOutput
	 * @param processor
	 */
	public DocumentReadyHandler(DocumentOutput documentOutput, Optional<DocumentProcessor> processor) {
		this.documentOutput = documentOutput;
		this.processor = processor;
	}

	/**
	 * Handles a give document as inputStream and call the document output
	 * 
	 * @param inputStream
	 * @param sender
	 */
	public void handle(String message, TransactionInfo transactionInfo) throws DocumentMessageException {

		if(transactionInfo == null)
			throw new IllegalArgumentException("TransactionInfo is required.");

		logger.debug("New document in {}", this.getClass().getName());

		String processedMessage = this.ExecuteProcessor(message, transactionInfo);
		logger.info("document {} successfully processed.", transactionInfo);

		// for each validation
		// if false throw ValidationEx

		logger.debug("Attempting to deliver the document.");
		this.documentOutput.send(processedMessage, transactionInfo);
		logger.info("document {} successfully delivered.", transactionInfo);

	}

	private String ExecuteProcessor(String content, TransactionInfo transactionInfo) {

		if (!processor.isPresent()) return content;
		
		logger.debug("Attempting to process the document.");
		return this.processor.get().processDocument(content, transactionInfo);
		
	}

}
