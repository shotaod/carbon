package org.carbon.sample.heroku.conf.auth.identity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.AuthEventListener;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.util.ResponseUtil;

/**
 * @author Shota Oda 2017/02/14.
 */
@Component
public class HerokuAuthFinisher implements AuthEventListener {
    @Override
    public void onAuth(String username, SessionContext sessionContext) {
        // noop
    }

    @Override
    public void onFail(HttpServletRequest request, HttpServletResponse response) {
        ResponseUtil.redirect(response, "/security/login");
    }
}
