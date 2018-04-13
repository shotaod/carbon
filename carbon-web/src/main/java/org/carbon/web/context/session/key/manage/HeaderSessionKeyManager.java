package org.carbon.web.context.session.key.manage;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.web.context.session.ShouldHandleRequest;
import org.carbon.web.context.session.key.SessionKey;
import org.carbon.web.context.session.key.SessionKeyGenerator;

/**
 * @author Shota.Oda 2018/03/03.
 */
public class HeaderSessionKeyManager extends AbstractSessionKeyManager {

    private String headerKey;

    public HeaderSessionKeyManager(
            ShouldHandleRequest shouldHandle,
            SessionKeyGenerator keyGenerator,
            String headerKey) {
        super(shouldHandle, keyGenerator);
        this.headerKey = headerKey;
    }

    @Override
    public SessionKey handle(HttpServletRequest request, HttpServletResponse response) {
        return Optional.ofNullable(request.getHeader(headerKey))
                .map(super::getKey)
                .orElseGet(() -> {
                    SessionKey key = generateKey();
                    String keyStr = key.key();
                    response.addHeader(headerKey, keyStr);
                    return key;
                });
    }
}
