package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.console;

import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties;
import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;

public class ConsoleInputTests {

    private ConsoleInput consoleInput;
    @Mock
    private DocumentReadyHandler documentReadyHandlerMock;

    @Mock
    private AccessProperties accessProperties;

    @Mock
    private PluginConfig pluginConfig;

    @Mock
    private TransactionInfo transactionInfoMock;

    @Before
    public void Init() {
        MockitoAnnotations.initMocks(this);
        try {
            Mockito.doNothing().when(documentReadyHandlerMock).handle("string",transactionInfoMock);
        } catch (DocumentMessageException e) {
            e.printStackTrace();
        }
        
        Mockito.when(pluginConfig.getSender()).thenReturn("console");
	    Mockito.when(accessProperties.getInput()).thenReturn(pluginConfig);
        
        consoleInput = new ConsoleInput(documentReadyHandlerMock, accessProperties);
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void run_normal_input_no_exception() throws Exception{
        ByteArrayInputStream in = new ByteArrayInputStream("string".getBytes());
        System.setIn(in);
        consoleInput.run();
    }
}