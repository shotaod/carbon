package org.carbon.sample.v2.app.api.rocketty.user;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.v2.ext.jooq.tables.RockettyUser;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLStateClass;

import static org.carbon.sample.v2.ext.jooq.tables.RockettyAuthClient.ROCKETTY_AUTH_CLIENT;
import static org.carbon.sample.v2.ext.jooq.tables.RockettyRanking.ROCKETTY_RANKING;
import static org.carbon.sample.v2.ext.jooq.tables.RockettyUser.ROCKETTY_USER;

/**
 * @author garden 2018/03/25.
 */
@Component
public class RockettyUserAppService {

    @Assemble
    private DSLContext jooq;

    public UserInfoDTO fetchUserInfo(Long clientId) {
        return jooq.select(ROCKETTY_RANKING.SCORE, ROCKETTY_USER.DISPLAY_NAME)
                .from(ROCKETTY_USER)
                .innerJoin(ROCKETTY_AUTH_CLIENT)
                    .on(ROCKETTY_USER.ROCKETTY_AUTH_CLIENT_ID.eq(ROCKETTY_AUTH_CLIENT.ID))
                .innerJoin(ROCKETTY_RANKING)
                    .on(ROCKETTY_USER.ID.eq(ROCKETTY_RANKING.ROCKETTY_USER_ID))
                .where(ROCKETTY_AUTH_CLIENT.ID.eq(clientId))
                .fetchOptional()
                .map(score_displayName -> new UserInfoDTO(score_displayName.value1(), score_displayName.value2()))
                .orElseGet(() -> new UserInfoDTO(null, null));
    }

    @Transactional
    public void saveUser(Long clientId, PostUserDTO postUserDTO) throws UserDuplicateException {
        Long userId = jooq.select(ROCKETTY_USER.ID)
                .from(ROCKETTY_USER)
                .innerJoin(ROCKETTY_AUTH_CLIENT)
                .on(ROCKETTY_USER.ROCKETTY_AUTH_CLIENT_ID.eq(ROCKETTY_AUTH_CLIENT.ID))
                .where(ROCKETTY_AUTH_CLIENT.ID.eq(clientId))
                    .fetchOptional()
                .map(Record1::value1)
                .orElse(null);
        try {
            String displayName = postUserDTO.getDisplayName();
            jooq.insertInto(ROCKETTY_USER)
                    .columns(ROCKETTY_USER.ID, ROCKETTY_USER.ROCKETTY_AUTH_CLIENT_ID, ROCKETTY_USER.DISPLAY_NAME)
                        .values(userId, clientId, displayName)
                    .onDuplicateKeyUpdate()
                        .set(ROCKETTY_USER.DISPLAY_NAME, displayName)
                    .execute();
        } catch (DataAccessException e) {
            if (e.sqlStateClass().equals(SQLStateClass.C23_INTEGRITY_CONSTRAINT_VIOLATION)) {
                throw new UserDuplicateException("username '" + postUserDTO.getDisplayName() + "' is duplicate");
            }
            throw e;
        }
    }
}
