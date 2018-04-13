package org.carbon.sample.v2.conf.translate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import org.carbon.component.annotation.Component;
import org.carbon.sample.v2.exception.JsonBadRequestException;
import org.carbon.web.translate.dto.ErrorTranslatableResult;
import org.carbon.web.translate.dto.Json;
import org.carbon.web.translate.dto.Translatable;
import org.carbon.web.translate.error.action.ThrowableHandleAction;

/**
 * @author Shota.Oda 2018/02/23.
 */
@Component
public class JsonBadRequestExceptionAction implements ThrowableHandleAction<JsonBadRequestException> {

    @Getter
    public static class BadRequest implements Json {
        private String message;

        public BadRequest(String message) {
            this.message = message;
        }
    }

    @Override
    public boolean supported(Throwable throwable) {
        return throwable instanceof JsonBadRequestException;
    }

    @Override
    public ErrorTranslatableResult<? extends Translatable> execute(HttpServletRequest request, JsonBadRequestException jsonException) {
        return new ErrorTranslatableResult<>(HttpServletResponse.SC_BAD_REQUEST, new BadRequest(jsonException.getMessage()));
    }
}
