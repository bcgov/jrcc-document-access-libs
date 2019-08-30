package ca.bc.gov.open.jrccaccess.autoconfigure;

import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.InputConfigMissingException;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.InvalidConfigException;
import ca.bc.gov.open.jrccaccess.autoconfigure.config.exceptions.OutputConfigMissingException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class AccessAutoConfigurationTests {
    private AccessAutoConfiguration aac;

    @Test
    public void constructor_with_valid_property_should_create_object() throws Exception{
        AccessProperties properties = new AccessProperties();
        properties.setInput(new AccessProperties.PluginConfig());
        properties.setOutput(new AccessProperties.PluginConfig());
        aac = new AccessAutoConfiguration(properties);
        assertNotNull(aac );
    }

    @Test(expected = InputConfigMissingException.class)
    public void constructor_with_null_input_property_should_throw_InputConfigMissingException() throws InvalidConfigException{
        AccessProperties properties = new AccessProperties();
        properties.setOutput(new AccessProperties.PluginConfig());
        properties.setInput(null);
        aac = new AccessAutoConfiguration(properties);
    }

    @Test(expected = OutputConfigMissingException.class)
    public void constructor_with_null_output_property_should_throw_OutputConfigMissingException() throws InvalidConfigException{
        AccessProperties properties = new AccessProperties();
        properties.setInput(new AccessProperties.PluginConfig());
        properties.setOutput(null);
        aac = new AccessAutoConfiguration(properties);
    }
}
