package org.carbon.sample.v2.app.api.rocketty.ranking;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.DecoderException;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.v2.app.api.rocketty.ranking.req.GetPageDTO;
import org.carbon.sample.v2.app.api.rocketty.ranking.req.PutRankingDTO;
import org.carbon.sample.v2.app.api.rocketty.ranking.resp.RockettyRankingsDTO;
import org.carbon.sample.v2.infra.secured.RockettyCipher;
import org.jooq.DSLContext;
import org.jooq.Record2;

import static org.carbon.sample.v2.ext.jooq.tables.RockettyAuthClient.ROCKETTY_AUTH_CLIENT;
import static org.carbon.sample.v2.ext.jooq.tables.RockettyRanking.ROCKETTY_RANKING;
import static org.carbon.sample.v2.ext.jooq.tables.RockettyUser.ROCKETTY_USER;

/**
 * @author Shota.Oda 2018/02/08.
 */
@Component
public class RockettyRankingAppService {

    @Assemble
    private DSLContext jooq;
    @Assemble
    private RockettyCipher cipher;
    @Assemble
    private ObjectMapper objectMapper;

    public RockettyRankingsDTO fetchRankings(GetPageDTO pageParam) {
        Integer page = pageParam.getP();
        Integer size = pageParam.getS();
        // getRankInfoRecords
        List<RockettyRankingsDTO.RankInfoDTO> rankInfo = jooq.select(ROCKETTY_RANKING.SCORE, ROCKETTY_USER.DISPLAY_NAME)
                .from(ROCKETTY_RANKING)
                .innerJoin(ROCKETTY_USER)
                .on(ROCKETTY_RANKING.ROCKETTY_USER_ID.eq(ROCKETTY_USER.ID))
                .where(ROCKETTY_RANKING.SCORE.isNotNull())
                .orderBy(ROCKETTY_RANKING.SCORE.desc().nullsLast())
                .limit(size * page, size)
                .fetch()
                .stream()
                .map(score_name -> new RockettyRankingsDTO.RankInfoDTO(score_name.value1(), score_name.value2()))
                .collect(Collectors.toList());
        // getPagingInfo
        Integer total = jooq.selectCount()
                .from(ROCKETTY_RANKING)
                .innerJoin(ROCKETTY_USER)
                .on(ROCKETTY_RANKING.ROCKETTY_USER_ID.eq(ROCKETTY_USER.ID))
                .where(ROCKETTY_RANKING.SCORE.isNotNull())
                .fetchOne().value1();
        int currentLastIndex = page * size + rankInfo.size();

        RockettyRankingsDTO.PageInfoDTO pageInfo = new RockettyRankingsDTO.PageInfoDTO(page != 0, currentLastIndex < total);
        return new RockettyRankingsDTO(rankInfo, pageInfo);
    }

    @Transactional
    public void saveRanking(Long clientId, PutRankingDTO putRanking) throws UserNotFoundException, IllegalRequestException {
        Record2<Long, Long> userId_rankingId = jooq.select(ROCKETTY_USER.ID, ROCKETTY_RANKING.ID)
                .from(ROCKETTY_AUTH_CLIENT)
                .leftJoin(ROCKETTY_USER)
                .on(ROCKETTY_AUTH_CLIENT.ID.eq(ROCKETTY_USER.ROCKETTY_AUTH_CLIENT_ID))
                .leftJoin(ROCKETTY_RANKING)
                .on(ROCKETTY_USER.ID.eq(ROCKETTY_RANKING.ROCKETTY_USER_ID))
                .where(ROCKETTY_AUTH_CLIENT.ID.eq(clientId))
                .fetchOne();
        Long userId = userId_rankingId.value1();
        Long rankingId = userId_rankingId.value2();
        if (userId == null) {
            throw new UserNotFoundException("no user found, create user first");
        }
        Integer score;
        try {
            score = doDecrypt(putRanking.getScore());
        } catch (GeneralSecurityException e) {
            throw new IllegalRequestException("illegal request", e);
        }
        jooq.insertInto(ROCKETTY_RANKING)
                .columns(ROCKETTY_RANKING.ID, ROCKETTY_RANKING.ROCKETTY_USER_ID, ROCKETTY_RANKING.SCORE)
                .values(rankingId, userId, score)
                .onDuplicateKeyUpdate()
                .set(ROCKETTY_RANKING.SCORE, score)
                .execute();
    }

    // ===================================================================================
    //                                                                    Score Decryption
    //                                                                          ==========
    @Getter
    @Setter
    public static class Score {
        private Integer value;
        private Long datetimeMills;
    }

    private Integer doDecrypt(String score) throws GeneralSecurityException {
        try {
            String plainScore = cipher.decrypt(score);
            Score scoreDate = objectMapper.readValue(plainScore, Score.class);
            // check datetime signature
            LocalDateTime target = Instant.ofEpochMilli(scoreDate.getDatetimeMills()).atZone(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime to = LocalDateTime.now(ZoneOffset.UTC);
            LocalDateTime from = to.minusSeconds(10);
            if (target.isBefore(from) || target.isAfter(to)) {
                throw new IllegalStateException("illegal datetime sign");
            }
            return scoreDate.getValue();
        } catch (NumberFormatException | DecoderException | IOException | IllegalStateException e) {
            throw new GeneralSecurityException(e);
        }
    }
}
