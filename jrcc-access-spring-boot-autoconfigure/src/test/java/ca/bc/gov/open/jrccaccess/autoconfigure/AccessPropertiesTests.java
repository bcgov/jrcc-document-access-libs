package ca.bc.gov.open.jrccaccess.autoconfigure;

import org.junit.BeforeClass;
import org.junit.Test;

import ca.bc.gov.open.jrccaccess.autoconfigure.common.Constants;
import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties;
import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;

import static org.junit.Assert.assertEquals;

public class AccessPropertiesTests {

	@Test
	public void with_sender_should_set_sender() {
		String sender = "bcgov";

        AccessProperties properties = new AccessProperties();
        properties.setInput(new AccessProperties.PluginConfig());
        PluginConfig pluginConfig = properties.getInput();

		pluginConfig.setSender(sender);

		assertEquals(pluginConfig.getSender(), sender);
    }

    @Test
    public void with_no_explicit_sender_should_return_default() {
        AccessProperties properties = new AccessProperties();
        properties.setInput(new AccessProperties.PluginConfig());
        PluginConfig pluginConfig = properties.getInput();

        assertEquals(Constants.UNKNOWN_SENDER, pluginConfig.getSender());
    }

    @Test
    public void with_plugin_should_set_plugin() {
        String plugin = "sftp";

        AccessProperties properties = new AccessProperties();
        properties.setInput(new AccessProperties.PluginConfig());
        PluginConfig pluginConfig = properties.getInput();

		pluginConfig.setPlugin(plugin);

		assertEquals(pluginConfig.getPlugin(), plugin);
    }

}
