package ca.bc.gov.open.jrccaccess.autoconfigure;

import ca.bc.gov.open.jrccaccess.autoconfigure.common.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents the custom configuration for the document flow lib
 * @author alexjoybc
 * @since 0.1.0
 *
 */
@ConfigurationProperties(prefix = "bcgov.access")
public class AccessProperties {

	
	// Represents the global output configuration.
	private PluginConfig output;
	
	// Represents the global input configuration.
	private PluginConfig input;
	
	
	/**
	 * Represents the plugin configuration
	 * @author alexjoybc
	 * @since 0.3.0
	 */
	public static class PluginConfig {
		
		
		// Collision ???
		private String documentType;
		
		private String plugin;

		private String sender;

		/**
		 * Gets the sender type
		 * @return
		 */
		public String getSender() {
			return StringUtils.isBlank(sender) ? Constants.UNKNOWN_SENDER : sender;
		}

		/**
		 * Sets the sender type
		 * @param sender
		 */
		public void setSender(String sender) {
			this.sender = sender;
		}
		
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
	public PluginConfig getOutput() {
		return output;
	}


	/**
	 * Sets the documentation output
	 * @param output
	 */
	public void setOutput(PluginConfig output) {
		this.output = output;
	}

	/**
	 * Gets the input configuration
	 * @return
	 */
	public PluginConfig getInput() {
		return input;
	}

	/**
	 * Sets the input configuration
	 * @param input
	 */
	public void setInput(PluginConfig input) {
		this.input = input;
	}
	
}
