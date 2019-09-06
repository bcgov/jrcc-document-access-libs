package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.http;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

public class AutoConfigurationTests {

    private AutoConfiguration autoConfiguration;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        autoConfiguration = new AutoConfiguration();
    }

    @Test(expected = Test.None.class )
    public void interceptor_should_be_added_to_registry(){
        InterceptorRegistry registryMock = Mockito.mock(InterceptorRegistry.class);
        Mockito.doReturn(Mockito.mock(InterceptorRegistration.class)).when(registryMock).addInterceptor(Mockito.any(HandlerInterceptor.class));
        autoConfiguration.addInterceptors(registryMock);
    }
}