package org.carbon.sample.heroku.web.auth;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.NonNull;
import org.apache.commons.text.RandomStringGenerator;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.heroku.exception.DuplicateEntityException;
import org.carbon.sample.heroku.ext.jooq.tables.daos.AuthClientDao;
import org.carbon.sample.heroku.ext.jooq.tables.pojos.AuthClient;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

/**
 * @author garden 2017/07/17.
 */
@Component
public class OAuth2Service {

    @Inject
    private AuthClientDao dao;

    private RandomStringGenerator stringGenerator;

    public OAuth2Service() {
        stringGenerator = new RandomStringGenerator.Builder()
                .withinRange('0', 'Z')
                .filteredBy(LETTERS, DIGITS)
                .build();
    }

    public List<AuthClientDto> fetchAllClient() {
        return dao.findAll().stream()
                .map(AuthClientDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public String registerClientHost(@NonNull String host) throws DuplicateEntityException {
        if (dao.fetchOneByClientHost(host) != null) {
            throw new DuplicateEntityException();
        }
        String clientId = generateClientId();
        AuthClient client = new AuthClient(null, host, clientId);
        dao.insert(client);
        return clientId;
    }

    private String generateClientId() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .concat(stringGenerator.generate(18));
    }

    public boolean validateClient(@NonNull String host, @NonNull String clientId) {
        AuthClient authClient = dao.fetchOneByClientHost(host);
        return Optional.ofNullable(authClient)
                .map(AuthClient::getClientId)
                .map(clientId::equals)
                .orElse(false);
    }
}
