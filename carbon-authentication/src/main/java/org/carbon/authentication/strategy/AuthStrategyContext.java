package org.carbon.authentication.strategy;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.session.SessionPool;

/**
 * @author Shota Oda 2016/10/29.
 */
@Component
public class AuthStrategyContext {

    @Assemble
    private SessionPool session;

    @Assemble
    private AuthStrategyRepository repository;

    public boolean existSecurity() {
        return !repository.isEmpty();
    }

    public Optional<AuthStrategy> findStrategy(HttpServletRequest request, HttpServletResponse response) {
        return repository.find(request, response);
    }
}
