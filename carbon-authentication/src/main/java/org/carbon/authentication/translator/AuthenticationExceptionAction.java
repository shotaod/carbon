package org.carbon.authentication.translator;

import javax.servlet.http.HttpServletRequest;

import org.carbon.authentication.exception.AccessProhibitedException;
import org.carbon.authentication.exception.AuthenticationException;
import org.carbon.authentication.exception.IllegalAuthenticationRequestException;
import org.carbon.authentication.exception.IllegalIdentityException;
import org.carbon.authentication.exception.IllegalSecretException;
import org.carbon.authentication.exception.event.AuthenticationExpiredEvent;
import org.carbon.authentication.strategy.AuthStrategy;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.request.RequestPool;
import org.carbon.web.translate.dto.ErrorTranslatableResult;
import org.carbon.web.translate.error.action.ThrowableHandleAction;

/**
 * @author Shota.Oda 2018/02/25.
 */
@Component
public class AuthenticationExceptionAction implements ThrowableHandleAction<AuthenticationException> {

    @Assemble
    private RequestPool requestPool;

    @Override
    public boolean supported(Throwable throwable) {
        return throwable instanceof AuthenticationException;
    }

    @Override
    public ErrorTranslatableResult<?> execute(HttpServletRequest request, AuthenticationException exception) {

        AuthStrategy authStrategy = requestPool.getByType(AuthStrategy.class)
                .orElseThrow(() -> new IllegalStateException("impossible"));

        if (exception instanceof AccessProhibitedException) {
            return convertToResult(authStrategy.translateProhibitNoAuth());
        }
        if (exception instanceof AuthenticationExpiredEvent) {
            return convertToResult(authStrategy.translateExpire());
        }
        if (exception instanceof IllegalAuthenticationRequestException) {
            return convertToResult(authStrategy.translateIllegalAuthRequest());
        }
        if (exception instanceof IllegalIdentityException) {
            return convertToResult(authStrategy.translateNoFoundIdentity());
        }
        if (exception instanceof IllegalSecretException) {
            return convertToResult(authStrategy.translateNoMatchSecret());
        }

        throw new UnsupportedOperationException();
    }

    private ErrorTranslatableResult<?> convertToResult(SignedTranslatable code_translatable) {
        return new ErrorTranslatableResult<>(code_translatable.getCode(), code_translatable.getTranslatable());
    }
}
