package org.carbon.sample.v2.app.api.rocketty.ranking.req;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * @author Shota.Oda 2018/02/08.
 */
@Setter
@Getter
public class PutRankingDTO {
    @NotNull
    @Length
    private String score;
}
