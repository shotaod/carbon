package org.carbon.sample.v2.web.oauth;

import java.util.Optional;
import java.util.Set;

import lombok.NonNull;
import org.apache.commons.text.RandomStringGenerator;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.v2.ext.jooq.tables.daos.AuthClientDao;
import org.carbon.sample.v2.ext.jooq.tables.pojos.AuthClient;
import org.carbon.sample.v2.util.OptionalStream;
import org.carbon.sample.v2.web.oauth.def.AuthScope;
import org.carbon.sample.v2.web.oauth.repository.AccessCode;
import org.carbon.sample.v2.web.oauth.repository.AccessCodeRepository;
import org.carbon.sample.v2.web.oauth.repository.AccessToken;
import org.carbon.sample.v2.web.oauth.repository.AccessTokenRepository;
import org.carbon.sample.v2.web.oauth.repository.Expiration;
import org.carbon.sample.v2.web.user.UserService;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Shota Oda 2017/07/17.
 */
@Component
public class OAuth2Service {

    @Inject
    private RandomStringGenerator stringGenerator;
    @Inject
    private UserService userService;
    @Inject
    private AuthClientDao dao;
    @Inject
    private AccessCodeRepository codeRepository;
    @Inject
    private AccessTokenRepository tokenRepository;

    public boolean checkUser(@NonNull OAuth2PermitForm form) {
        return userService.findByEmail(form.getEmail())
                .map(user -> BCrypt.checkpw(form.getPassword(), user.getPassword()))
                .orElse(false);
    }

    public boolean validateClient(@NonNull String host, @NonNull String clientId) {
        AuthClient authClient = dao.fetchOneByClientHost(host);
        return Optional.ofNullable(authClient)
                .map(AuthClient::getClientId)
                .map(clientId::equals)
                .orElse(false);
    }

    public String publishAuthorizationCode(@NonNull OAuth2PermitForm form) {
        String code = stringGenerator.generate(10);

        AccessCode accessCode = new AccessCode(form.getHost(), code, form.getScopes());

        codeRepository.save(accessCode);

        return code;
    }

    public AccessToken publishAccessToken(@NonNull OAuth2TokenForm form) throws OAuth2Exception {

        return OptionalStream
                .of(dao.fetchOneByClientId(form.getClient_id()))
                    .whenEmptyThrow(OAuth2Exception::invalidClient)
                .map(authClient -> codeRepository.find(authClient.getClientHost(), form.getCode()))
                .filter(Expiration::isValid)
                    .whenEmptyThrow(OAuth2Exception::unauthorizedClient)
                .map(accessCode -> {
                    String host = accessCode.getHost();
                    String accessToken = stringGenerator.generate(10);
                    String refreshToken = stringGenerator.generate(10);
                    Set<AuthScope> authScopes = accessCode.getAuthScopes();
                    return new AccessToken(accessToken, refreshToken, host, authScopes);
                })
                .peek(accessToken -> tokenRepository.save(accessToken))
                .orElseThrow(OAuth2Exception::invalidClient);
    }

    public AccessToken refreshAccessToken(@NonNull OAuth2TokenForm form) {
        return null;
    }
}
