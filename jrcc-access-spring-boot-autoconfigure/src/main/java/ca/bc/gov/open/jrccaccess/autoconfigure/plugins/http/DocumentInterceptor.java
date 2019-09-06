package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.http;

import ca.bc.gov.open.jrccaccess.autoconfigure.common.Constants;
import org.slf4j.MDC;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An interceptor for adding MDC logging info for http request.
 */
public class DocumentInterceptor implements HandlerInterceptor {
    // Called before handler method invocation
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
                             Object handler) throws Exception {
        if(req instanceof MultipartHttpServletRequest ){
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest) req;
            MultipartFile file = multiRequest.getFile("file");
            MDC.put(Constants.MDC_KEY_FILENAME,file.getOriginalFilename());
        }
        return true;
    }

    // Called after handler method request completion, before rendering the view
    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res,
                           Object handler, ModelAndView model)  throws Exception {
    }

    // Called after rendering the view
    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res,
                                Object handler, Exception ex)  throws Exception {
        MDC.clear();
    }
}
