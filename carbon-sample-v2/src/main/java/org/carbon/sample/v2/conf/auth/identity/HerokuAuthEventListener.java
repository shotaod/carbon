package org.carbon.sample.v2.conf.auth.identity;

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
public class HerokuAuthEventListener implements AuthEventListener<HerokuAuthIdentity> {

    @Override
    public void onAuth(HerokuAuthIdentity identity, SessionContext sessionContext) {
    }

    @Override
    public void onFail(HttpServletRequest request, HttpServletResponse response) {
        ResponseUtil.redirect(response, "/user/login");
    }
}
