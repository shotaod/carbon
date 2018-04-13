package org.carbon.web.translate.error.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.web.exception.request_mapping.JsonRequestMappingException;
import org.carbon.web.exception.request_mapping.RequestMappingException;
import org.carbon.web.exception.request_mapping.UnsupportedRequestException;
import org.carbon.web.translate.dto.ErrorTranslatableResult;
import org.carbon.web.translate.dto.Json;
import org.carbon.web.translate.dto.Text;
import org.carbon.web.translate.dto.Translatable;

/**
 * @author Shota.Oda 2018/03/03.
 */
@Component
public class RequestMappingExceptionAction implements ThrowableHandleAction<RequestMappingException> {

    @Override
    public Integer priority() {
        return LOW_PRIORITY;
    }

    @Override
    public boolean supported(Throwable throwable) {
        return throwable instanceof RequestMappingException;
    }

    public static class BadRequestJson implements Json {
        private String message;

        public BadRequestJson(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @Override
    public ErrorTranslatableResult<? extends Translatable> execute(HttpServletRequest request, RequestMappingException throwable) {
        if (throwable instanceof UnsupportedRequestException) {
            return new ErrorTranslatableResult<>(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, new Text("unsupported"));
        }
        int badCode = HttpServletResponse.SC_BAD_REQUEST;
        if (throwable instanceof JsonRequestMappingException) {
            return new ErrorTranslatableResult<>(badCode, new BadRequestJson("bad request"));
        }
        //if (throwable instanceof MultipartFormRequestMappingException) {
        return new ErrorTranslatableResult<>(badCode, new Text("bad request"));
    }
}
