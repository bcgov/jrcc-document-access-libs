package ca.bc.gov.open.jrccaccess.libs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.beans.factory.annotation.Value;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the transaction information
 * @author alexjoybc
 *
 */
public class TransactionInfo {

	/**
	 * the original sender (organization_
	 */
	@Value("${bcgov.access.sender}")
	private String sender;
	
	/**
	 * the original fileName
	 */
	private String fileName;
	
	/**
	 * the date the file was received on
	 */
	private LocalDateTime receivedOn;

	@JsonCreator
	public TransactionInfo(
			@JsonProperty("fileName")String fileName, 
			@JsonProperty("sender")String sender, 
			@JsonProperty("receivedOn")LocalDateTime receivedOn) {
		
		if(sender == null || sender.isEmpty()) throw new IllegalArgumentException("sender");
		if(fileName == null || fileName.isEmpty()) throw new IllegalArgumentException("fileName");	
		if(receivedOn == null) throw new IllegalArgumentException("receivedOn");
		
		this.sender = sender;
		this.fileName = fileName;
		this.receivedOn = receivedOn;

	}
	
	public String getSender() {
		return sender;
	}

	public String getFileName() {
		return fileName;
	}

	public LocalDateTime getReceivedOn() {
		return receivedOn;
	}
	
	@Override
	public String toString() {		
		return	MessageFormat.format("Transaction sent from [{0}] on [{1}], fileName [{2}]", this.sender, this.receivedOn.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), this.fileName);
	}
	
}
