package ca.gov.bc.open.jrccaccess.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents the custom configuration for the document flow lib
 * @author alexjoybc
 * @since 0.1.0
 *
 */
@ConfigurationProperties(prefix = "bcgov.access")
public class AccessProperties {

	
	// Represent the global output config
	private Output output;
	
	
	/**
	 * Represents the globla output
	 * @author alexjoybc
	 * @since 0.3.0
	 */
	public static class Output {
		
		public String documentType;
		
		public String plugin;
		
		/**
		 * Gets the plugin type
		 * @return
		 */
		public String getPlugin() {
			return plugin;
		}

		/**
		 * Sets the plugin type
		 * @param plugin
		 */
		public void setPlugin(String plugin) {
			this.plugin = plugin;
		}

		/**
		 * Sets the document type
		 * @return
		 */
		public String getDocumentType() {
			return documentType;
		}

		/**
		 * Gets the document type
		 * @param documentType
		 */
		public void setDocumentType(String documentType) {
			this.documentType = documentType;
		}
		
	}


	/**
	 * Gets the output configuration
	 * @return
	 */
	public Output getOutput() {
		return output;
	}


	/**
	 * Sets the documentation output
	 * @param output
	 */
	public void setOutput(Output output) {
		this.output = output;
	}
	
	
	
}
