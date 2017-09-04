package org.carbon.sample.heroku.conf.auth;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Component;
import org.carbon.authentication.AuthRequestMapper;

/**
 * @author Shota Oda 2016/11/04.
 */
@Component
public class HerokuAuthRequestMapper implements AuthRequestMapper{

    @Override
    public Optional<AuthInfo> map(HttpServletRequest request) {
        Object usernameObj = request.getParameter("email");
        Object passwordObj = request.getParameter("password");
        String username = Optional.ofNullable(usernameObj).map(Object::toString).orElse("");
        String password = Optional.ofNullable(passwordObj).map(Object::toString).orElse("");

        return Optional.of(new AuthInfo(username, password));
    }
}
