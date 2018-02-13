package org.carbon.sample.v2.web.api.rocketty;

import org.carbon.component.annotation.Inject;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.RequestParam;
import org.carbon.web.annotation.Validate;
import org.carbon.web.core.validation.HandyValidationResult;
import org.carbon.web.def.HttpMethod;

/**
 * @author garden 2018/02/08.
 */
@Controller("/api/v1/rocketty")
public class RockettyRankingController {

    @Inject
    private RockettyRankingAppService appService;

    @Action(url = "/ranks", method = HttpMethod.GET)
    public Object getRank(
            @Validate @RequestParam PageParamDTO pageParam,
            HandyValidationResult vr
    ) {
        if (vr.existError()) {
            return StatusResponseDTO.badRequest(vr);
        }

        return appService.queryRankings(pageParam);
    }

    @Action(url = "/ranks", method = HttpMethod.POST)
    public Object postRank(
            @Validate @RequestBody PostRankingDTO postRanking,
            HandyValidationResult vr
    ) {
        if (vr.existError()) {
            return StatusResponseDTO.badRequest(vr);
        }

        String internalKey = appService.saveRanking(postRanking);
        return StatusResponseDTO.success(internalKey);
    }

    @Action(url = "/ranks", method = HttpMethod.PUT)
    public Object putRank(
            @Validate @RequestBody PutRankingDTO postRanking,
            HandyValidationResult vr
    ) {
        if (vr.existError()) {
            return StatusResponseDTO.badRequest(vr);
        }

        try {
            appService.updateRanking(postRanking);
            return StatusResponseDTO.success();
        } catch (ResourceNotFoundException e) {
            return StatusResponseDTO.badRequest("illegal user_identifier");
        }
    }
}
