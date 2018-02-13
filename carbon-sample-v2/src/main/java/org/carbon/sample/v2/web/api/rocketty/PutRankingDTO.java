package org.carbon.sample.v2.web.api.rocketty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author garden 2018/02/08.
 */
@Setter
@Getter
public class PutRankingDTO {
    @NotBlank
    @Length(min = 127, max = 127)
    private String internal_user_id;
    @NotNull
    @Min(1)
    private Integer score;
}
