package org.carbon.sample.v2.app.api.rocketty.auth;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.commons.text.RandomStringGenerator;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.v2.app.api.rocketty.SuccessMessageDTO;
import org.carbon.sample.v2.app.api.rocketty.auth.exception.IllegalClientAuthException;
import org.carbon.sample.v2.app.api.rocketty.auth.req.AuthConfirmDTO;
import org.carbon.sample.v2.app.api.rocketty.auth.req.AuthRegisterDTO;
import org.carbon.sample.v2.app.api.rocketty.auth.resp.AuthInfoDTO;
import org.jooq.DSLContext;
import org.jooq.Record1;

import static org.carbon.sample.v2.ext.jooq.tables.RockettyAuthClient.ROCKETTY_AUTH_CLIENT;

/**
 * @author Shota.Oda 2018/02/15.
 */
@Component
public class RockettyAuthAppService {

    // prefix = {apiVer}_{appVer}_{Other}_
    private static final String idPrefix = "p1_a1_CBN_";

    @Assemble
    private DSLContext jooq;
    @Assemble
    private RockettyProp rockettyProp;
    @Assemble
    private RandomStringGenerator randomStringGenerator;

    @Transactional
    public AuthInfoDTO registerClient(AuthRegisterDTO authRegisterDTO) throws IllegalClientAuthException {
        if (!authRegisterDTO.getAppSecret().equals(rockettyProp.getSecret())) {
            throw new IllegalClientAuthException("illegal client secret");
        }

        String clientId = idPrefix + randomStringGenerator.generate(245);
        String secret = randomStringGenerator.generate(255);
        LocalDateTime expireAt = LocalDateTime.now().plus(5, ChronoUnit.MINUTES);
        jooq.insertInto(ROCKETTY_AUTH_CLIENT)
                .columns(ROCKETTY_AUTH_CLIENT.CLIENT_ID, ROCKETTY_AUTH_CLIENT.CLIENT_SECRET, ROCKETTY_AUTH_CLIENT.EXPIRE_AT)
                .values(clientId, secret, expireAt)
                .execute();

        return new AuthInfoDTO(clientId, secret);
    }

    @Transactional
    public SuccessMessageDTO confirmClient(AuthConfirmDTO authConfirmDTO) throws IllegalClientAuthException {
        String clientId = authConfirmDTO.getClientId();
        String clientSecret = authConfirmDTO.getClientSecret();
        Record1<Long> record = jooq.select(ROCKETTY_AUTH_CLIENT.ID)
                .from(ROCKETTY_AUTH_CLIENT)
                .where(ROCKETTY_AUTH_CLIENT.CLIENT_ID.eq(clientId))
                .and(ROCKETTY_AUTH_CLIENT.CLIENT_SECRET.eq(clientSecret))
                .and(ROCKETTY_AUTH_CLIENT.EXPIRE_AT.greaterOrEqual(LocalDateTime.now()))
                .fetchOne();
        if (record == null) {
            throw new IllegalClientAuthException("not found auth information for request");
        }

        jooq.update(ROCKETTY_AUTH_CLIENT)
                .set(ROCKETTY_AUTH_CLIENT.VALID, true)
                .where(ROCKETTY_AUTH_CLIENT.ID.eq(record.value1()))
                .execute();

        return new SuccessMessageDTO("success registered");
    }
}
