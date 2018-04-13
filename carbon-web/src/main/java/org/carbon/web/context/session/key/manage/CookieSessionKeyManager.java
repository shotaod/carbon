package org.carbon.web.context.session.key.manage;

import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.web.context.session.ShouldHandleRequest;
import org.carbon.web.context.session.key.SessionKey;
import org.carbon.web.context.session.key.SessionKeyGenerator;

/**
 * @author Shota.Oda 2018/03/03.
 */
public class CookieSessionKeyManager extends AbstractSessionKeyManager {

    private String cooKey;

    public CookieSessionKeyManager(
            ShouldHandleRequest shouldHandle,
            SessionKeyGenerator keyGenerator,
            String cooKey) {
        super(shouldHandle, keyGenerator);
        this.cooKey = cooKey;
    }

    @Override
    public SessionKey handle(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            cookies = new Cookie[]{};
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cooKey))
                .findFirst()
                .map(Cookie::getValue)
                .map(super::getKey)
                .orElseGet(() -> {
                    SessionKey key = generateKey();
                    String keyStr = key.key();
                    response.addCookie(new Cookie(cooKey, keyStr));
                    return key;
                });
    }
}
