package org.carbon.sample.auth.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.AuthEventListener;
import org.carbon.authentication.AuthIdentity;
import org.carbon.component.annotation.Component;
import org.carbon.sample.auth.basic.identity.SampleBasicAuthIdentity;
import org.carbon.web.context.session.SessionContext;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class BasicAuthEvent implements AuthEventListener<SampleBasicAuthIdentity> {
    private static final String Header_Auth = "WWW-Authenticate";

    @Override
    public void onAuth(SampleBasicAuthIdentity username, SessionContext session) {

    }

    @Override
    public void onFail(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(Header_Auth, "Basic realm=Basic");
    }
}
