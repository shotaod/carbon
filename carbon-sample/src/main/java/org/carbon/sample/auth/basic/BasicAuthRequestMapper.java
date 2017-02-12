package org.carbon.sample.auth.basic;

import org.apache.tomcat.util.codec.binary.Base64;
import org.carbon.component.annotation.Component;
import org.carbon.web.auth.AuthRequestMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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
                    String[] info = new String(Base64.decodeBase64(base64)).split(":");
                    if (info.length != 2) {
                        return Optional.empty();
                    }
                    return Optional.of(new AuthInfo(info[0], info[1]));
                });
    }
}
