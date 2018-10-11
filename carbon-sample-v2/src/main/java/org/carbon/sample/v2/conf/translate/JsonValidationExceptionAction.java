package org.carbon.sample.v2.conf.translate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.request.RequestPool;
import org.carbon.web.core.args.ValidateAggregator;
import org.carbon.web.core.args.ValidateAggregator.ValidationFailureException;
import org.carbon.web.translate.dto.ErrorTranslatableResult;
import org.carbon.web.translate.dto.Translatable;
import org.carbon.web.translate.error.action.ThrowableHandleAction;

/**
 * @author Shota.Oda 2018/02/23.
 */
@Component
public class JsonValidationExceptionAction implements ThrowableHandleAction<ValidationFailureException> {
    private static final String API_ROOT = "/api/v1";

    @Assemble
    private RequestPool requestPool;

    @Override
    public boolean supported(Throwable throwable) {
        // taking account of path information
        boolean isApiRequest = requestPool.getRequest().getPathInfo().startsWith(API_ROOT);
        return isApiRequest && throwable instanceof ValidationFailureException;
    }

    @Override
    public ErrorTranslatableResult<? extends Translatable> execute(HttpServletRequest request, ValidationFailureException exception) {
        return new ErrorTranslatableResult<>(HttpServletResponse.SC_BAD_REQUEST, new Message(exception.getMessage()));
    }
}
