package org.carbon.sample.v2.conf.translate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.request.RequestPool;
import org.carbon.web.translate.dto.ErrorTranslatableResult;
import org.carbon.web.translate.dto.Translatable;
import org.carbon.web.translate.error.action.ThrowableHandleAction;

/**
 * @author Shota.Oda 2018/02/23.
 */
@Component
public class JsonInternalServerErrorAction implements ThrowableHandleAction<Throwable> {
    private static final String API_ROOT = "/api/v1";

    @Assemble
    private RequestPool requestPool;

    @Override
    public boolean supported(Throwable throwable) {
        // determine by path
        return requestPool.getRequest().getPathInfo().startsWith(API_ROOT);
    }

    @Override
    public Integer priority() {
        return LOW_PRIORITY;
    }

    @Override
    public ErrorTranslatableResult<? extends Translatable> execute(HttpServletRequest request, Throwable jsonException) {
        return new ErrorTranslatableResult<>(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new Message(jsonException.getMessage()));
    }
}
