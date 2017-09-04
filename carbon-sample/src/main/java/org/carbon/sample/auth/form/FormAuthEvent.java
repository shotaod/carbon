package org.carbon.sample.auth.form;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.AuthEventListener;
import org.carbon.authentication.AuthIdentifier;
import org.carbon.authentication.AuthIdentity;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.auth.form.identity.FormAuthIdentifier;
import org.carbon.sample.auth.form.identity.FormAuthIdentity;
import org.carbon.sample.web.session.SessionInfo;
import org.carbon.web.context.session.SessionContext;
import org.carbon.web.util.ResponseUtil;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class FormAuthEvent implements AuthEventListener<FormAuthIdentity> {

    @Override
    public void onAuth(FormAuthIdentity identity, SessionContext sessionContext) {
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setUsername(identity.username());
        sessionInfo.setDateTime(LocalDateTime.now());
        sessionContext.setObject(sessionInfo);
    }

    @Override
    public void onFail(HttpServletRequest request, HttpServletResponse response) {
        ResponseUtil.redirect(response, "/form/login");
    }
}
