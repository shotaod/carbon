package org.carbon.sample.v2.app.api.rocketty.ranking.resp;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.carbon.web.translate.dto.Json;

/**
 * @author Shota.Oda 2018/02/08.
 */
@AllArgsConstructor
@Getter
public class RockettyRankingsDTO implements Json {
    @AllArgsConstructor
    @Getter
    public static class RankInfoDTO {
        private int score;
        private String displayName;
    }

    @AllArgsConstructor
    @Getter
    public static class PageInfoDTO {
        private boolean prev;
        private boolean next;
    }

    private List<RankInfoDTO> ranks;
    private PageInfoDTO page;
}
