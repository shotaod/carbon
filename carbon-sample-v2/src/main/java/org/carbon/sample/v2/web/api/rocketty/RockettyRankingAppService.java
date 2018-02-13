package org.carbon.sample.v2.web.api.rocketty;

import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;
import org.apache.commons.text.RandomStringGenerator;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.v2.ext.jooq.tables.daos.RockettyRankingDao;
import org.carbon.sample.v2.ext.jooq.tables.pojos.RockettyRanking;
import org.jooq.DSLContext;

import static org.carbon.sample.v2.ext.jooq.tables.RockettyRanking.ROCKETTY_RANKING;

/**
 * @author garden 2018/02/08.
 */
@Component
public class RockettyRankingAppService {

    @Inject
    private DSLContext jooq;
    @Inject
    private RockettyRankingDao dao;
    @Inject
    private RandomStringGenerator randomStringGenerator;

    public RockettyRankingDTO queryRankings(@NonNull PageParamDTO pageParam) {
        Integer page = pageParam.getP();
        Integer size = pageParam.getS();
        List<RockettyRankingDTO.RockettyRankingDetailDTO> rankingDetails = jooq
                .select(ROCKETTY_RANKING.SCORE, ROCKETTY_RANKING.DISPLAY_NAME)
                .from(ROCKETTY_RANKING)
                .where(ROCKETTY_RANKING.SCORE.isNotNull())
                .orderBy(ROCKETTY_RANKING.SCORE.desc())
                .limit(page, size)
                .fetchInto(RockettyRanking.class)
                .stream()
                .map(pojo -> new RockettyRankingDTO.RockettyRankingDetailDTO(pojo.getScore(), pojo.getDisplayName()))
                .collect(Collectors.toList());
        return new RockettyRankingDTO(page, rankingDetails);
    }

    @Transactional
    public String saveRanking(PostRankingDTO postRankingDTO) {
        String internalKey = randomStringGenerator.generate(127);
        String displayName = postRankingDTO.getDisplay_name();
        RockettyRanking rankRecord = new RockettyRanking(null, internalKey, null, displayName);
        dao.insert(rankRecord);
        return internalKey;
    }

    @Transactional
    public void updateRanking(PutRankingDTO putRanking) throws ResourceNotFoundException {
        String internalUserId = putRanking.getInternal_user_id();
        Integer score = putRanking.getScore();
        RockettyRanking rankRecord = dao.fetchOneByInternalUserId(internalUserId);
        if (rankRecord == null) {
            throw new ResourceNotFoundException();
        }
        rankRecord.setScore(score);
        dao.update(rankRecord);
    }
}
