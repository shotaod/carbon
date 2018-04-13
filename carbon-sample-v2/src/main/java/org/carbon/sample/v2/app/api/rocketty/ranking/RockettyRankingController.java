package org.carbon.sample.v2.app.api.rocketty.ranking;

import org.carbon.component.annotation.Assemble;
import org.carbon.sample.v2.app.api.rocketty.SuccessMessageDTO;
import org.carbon.sample.v2.app.api.rocketty.ranking.req.GetPageDTO;
import org.carbon.sample.v2.app.api.rocketty.ranking.req.PutRankingDTO;
import org.carbon.sample.v2.conf.auth.api.RockettyClientIdentity;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.RequestParam;
import org.carbon.web.annotation.Validate;
import org.carbon.web.annotation.scope.SessionScope;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.Json;

/**
 * @author Shota.Oda 2018/02/08.
 */
@Controller("/api/v1/rocketty/ranks")
public class RockettyRankingController {

    @Assemble
    private RockettyRankingAppService appService;

    @Action(path = "", method = HttpMethod.GET)
    public Json getRank(
            @Validate @RequestParam GetPageDTO pageParam
    ) {
        return appService.fetchRankings(pageParam);
    }

    /**
     * save score
     *
     * @param clientIdentity from session
     * @param putRankingDTO  from request
     * @throws UserNotFoundException   when user not found
     * @throws IllegalRequestException when request is illegal(ex. not satisfy encryption)
     */
    @Action(path = "", method = HttpMethod.PUT)
    public Json putRank(
            @SessionScope RockettyClientIdentity clientIdentity,
            @Validate @RequestBody PutRankingDTO putRankingDTO
    ) throws
            UserNotFoundException,
            IllegalRequestException {
        appService.saveRanking(clientIdentity.getId(), putRankingDTO);
        return new SuccessMessageDTO("success to update score");
    }
}
