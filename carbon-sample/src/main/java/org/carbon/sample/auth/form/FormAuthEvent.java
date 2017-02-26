package org.carbon.sample.auth.form;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.web.session.SessionInfo;
import org.carbon.web.auth.AuthEventListener;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class FormAuthEvent implements AuthEventListener {
    @Inject
    private SessionContext sessionContext;
    @Override
    public void onAuth(String username, SessionContext sessionContext) {
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setUsername(username);
        sessionInfo.setDateTime(LocalDateTime.now());
        sessionContext.setObject(sessionInfo);
    }

    @Override
    public void onFail(HttpServletRequest request, HttpServletResponse response) {
        ResponseUtil.redirect(response, "/form/login");
    }
}
