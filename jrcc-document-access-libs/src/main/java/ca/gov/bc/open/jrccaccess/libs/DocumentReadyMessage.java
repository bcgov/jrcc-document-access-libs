package ca.gov.bc.open.jrccaccess.libs;

import java.text.MessageFormat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a document ready message that can be publised to a topic
 * @author 177226
 *
 */
public class DocumentReadyMessage {

	/**
	 * the transaction information
	 */
	private TransactionInfo transactionInfo;
	
	/**
	 * The document information
	 */
	private DocumentInfo documentInfo;
	
	/**
	 * The storage properties
	 */
	private DocumentStorageProperties documentStorageProperties;

	/**
	 * Default constructor
	 * @param transactionInfo A TransactionInfo
	 * @param documentInfo A DocumentInfo
	 * @param documentStorageProperties A DocumentStorageProperties
	 */
	@JsonCreator
	public DocumentReadyMessage(
			@JsonProperty("transactionInfo")TransactionInfo transactionInfo, 
			@JsonProperty("documentInfo")DocumentInfo documentInfo, 
			@JsonProperty("documentStorageProperties")DocumentStorageProperties documentStorageProperties) {
		
		
		if(transactionInfo == null) throw new IllegalArgumentException("transactionInfo");
		if(documentInfo == null) throw new IllegalArgumentException("transactionInfo");
		if(documentStorageProperties == null) throw new IllegalArgumentException("transactionInfo");
		
		this.transactionInfo = transactionInfo;
		this.documentInfo = documentInfo;
		this.documentStorageProperties = documentStorageProperties;
		
	}
	
	
	public TransactionInfo getTransactionInfo() {
		return transactionInfo;
	}

	public DocumentInfo getDocumentInfo() {
		return documentInfo;
	}

	public DocumentStorageProperties getDocumentStorageProperties() {
		return documentStorageProperties;
	}
	
	@Override
	public String toString() {
		
		return MessageFormat.format("Document Message Ready for Transaction with [{0}] on document type [{1}], stored with key [{2}]", this.transactionInfo.getSender(), this.documentInfo.getType(), this.documentStorageProperties.getKey());
		
	}
	
	
	
}
