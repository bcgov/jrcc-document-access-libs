package ca.bc.gov.open.jrccaccess.autoconfigure;

import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.InputConfigMissingException;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.InvalidConfigException;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.OutputConfigMissingException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;


public class AccessAutoConfigurationTests {
    private AccessAutoConfiguration sut;

    @Test
    public void constructor_with_valid_property_should_create_object() throws Exception{
        AccessProperties properties = new AccessProperties();
        properties.setInput(new AccessProperties.PluginConfig());
        properties.setOutput(new AccessProperties.PluginConfig());
        sut = new AccessAutoConfiguration(properties);
        assertNotNull(sut );
    }

    @Test(expected = InputConfigMissingException.class)
    public void constructor_with_null_input_property_should_throw_InputConfigMissingException() throws InvalidConfigException{
        AccessProperties properties = new AccessProperties();
        properties.setOutput(new AccessProperties.PluginConfig());
        properties.setInput(null);
        sut = new AccessAutoConfiguration(properties);
    }

    @Test(expected = OutputConfigMissingException.class)
    public void constructor_with_null_output_property_should_throw_OutputConfigMissingException() throws InvalidConfigException{
        AccessProperties properties = new AccessProperties();
        properties.setInput(new AccessProperties.PluginConfig());
        properties.setOutput(null);
        sut = new AccessAutoConfiguration(properties);
    }

    @Test
    public void input_config_returns_valid_input_plugin_config() throws InvalidConfigException {
        AccessProperties properties = new AccessProperties();
        properties.setInput(new AccessProperties.PluginConfig());
        properties.setOutput(new AccessProperties.PluginConfig());
        sut = new AccessAutoConfiguration(properties);
        assertEquals(sut.inputConfig(), properties.getInput());
    }
}
