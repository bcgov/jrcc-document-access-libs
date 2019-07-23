package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.console;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * represents console output properties
 * @author alexjoybc
 * @since 0.3.1
 */
@ConfigurationProperties(prefix = "bcgov.access.output.console")
public class ConsoleOutputProperties {
	
	private String format;

	/**
	 * Sets the format of the document
	 * @return
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * gets the format of the document
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	
	
}
