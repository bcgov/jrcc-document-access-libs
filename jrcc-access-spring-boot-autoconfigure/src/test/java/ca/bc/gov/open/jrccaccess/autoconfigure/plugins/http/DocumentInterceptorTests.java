package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.http;

import ca.bc.gov.open.jrccaccess.autoconfigure.common.Constants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentInterceptorTests {

    private DocumentInterceptor interceptor;

    @Mock
    private HttpServletResponse response;
    @Mock
    private Object o;
    @Mock
    private MultipartHttpServletRequest request;
    @Mock
    private MultipartFile multipartFile;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        interceptor = new DocumentInterceptor();
        Mockito.doReturn("filename").when(multipartFile).getOriginalFilename();
        Mockito.when(request.getFile("file")).thenReturn(multipartFile);
    }

    @Test
    public void filename_should_be_added_to_MDC_after_preHandle() throws Exception{
        interceptor.preHandle(request, response, o);
        String filename = MDC.get(Constants.MDC_KEY_FILENAME);
        assertEquals("filename",filename);
    }

    @Test
    public void no_filename_added_when_request_is_not_multipart_after_preHandle() throws Exception{
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        interceptor.preHandle(request, response, o);
        String filename = MDC.get(Constants.MDC_KEY_FILENAME);
        assertNull(filename);
    }

    @Test
    public void no_filename_in_MDC_afterCompletion() throws Exception{
        Exception e = Mockito.mock(Exception.class);
        interceptor.preHandle(request, response, o);
        String filename = MDC.get(Constants.MDC_KEY_FILENAME);
        assertEquals("filename",filename);
        interceptor.afterCompletion(request, response, o, e);
        filename = MDC.get(Constants.MDC_KEY_FILENAME);
        assertNull(filename);
    }

    @Test(expected = Test.None.class )
    public void postHandle_do_nothing() throws Exception{
        interceptor.postHandle(request,response,o,null);
    }
}