package ca.bc.gov.open.jrccaccess.libs.processing;

import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;

/**
 * The DocumentProcessor interface provides implementation details for processing documents
 * @author alexjoybc
 * @since 0.0.1
 */
public interface DocumentProcessor {

	/**
	 * process a given document.
	 * @param content
	 * @param transactionInfo
	 * @return
	 */
	String processDocument(String content, TransactionInfo transactionInfo);
	
	
}
