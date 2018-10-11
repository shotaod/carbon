package org.carbon.sample.v2.conf.auth.api;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.carbon.authentication.conf.rule.APIOrientedRule;
import org.carbon.authentication.strategy.request.AuthRequest;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;

/**
 * @author garden 2018/03/25.
 */
@Component
public class RockettyAuthRequestMapping implements APIOrientedRule.ThrowableMapRequest {

    @Getter
    @Setter
    public static class RockettyAuthRequestDTO {
        private String clientId;
        private String clientSecret;
    }

    @Assemble
    private ObjectMapper objectMapper;

    @Override
    public Optional<AuthRequest> map(HttpServletRequest request) throws Throwable {
        RockettyAuthRequestDTO dto = objectMapper.readValue(request.getReader(), RockettyAuthRequestDTO.class);
        return Optional.of(new AuthRequest(dto.getClientId(), dto.getClientSecret()));
    }
}
