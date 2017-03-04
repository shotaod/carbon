package org.carbon.sample.auth.basic;

import java.util.Base64;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.carbon.authentication.AuthRequestMapper;
import org.carbon.component.annotation.Component;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class BasicAuthRequestMapper implements AuthRequestMapper {
    private static final String Header_Auth = "Authorization";
    private static final String Auth_Basic = "Basic";

    @Override
    public Optional<AuthInfo> map(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(Header_Auth))
                .filter(header -> header.startsWith(Auth_Basic))
                .flatMap(header -> {
                    String base64 = header.replace(Auth_Basic, "").trim();
                    String[] info = new String(Base64.getDecoder().decode(base64)).split(":");
                    if (info.length != 2) {
                        return Optional.empty();
                    }
                    return Optional.of(new AuthInfo(info[0], info[1]));
                });
    }
}
