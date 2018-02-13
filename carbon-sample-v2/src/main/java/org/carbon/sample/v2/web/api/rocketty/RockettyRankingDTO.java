package org.carbon.sample.v2.web.api.rocketty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author garden 2018/02/08.
 */
@AllArgsConstructor
@Getter
public class RockettyRankingDTO {
    @AllArgsConstructor
    @Getter
    public static class RockettyRankingDetailDTO {
        private int score;
        private String name;
    }
    private int page;
    private List<RockettyRankingDetailDTO> details;
}
