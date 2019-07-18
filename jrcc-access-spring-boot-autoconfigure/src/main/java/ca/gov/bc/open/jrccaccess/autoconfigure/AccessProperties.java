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
		
		
		// Colision ???
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
