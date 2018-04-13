package org.carbon.sample.v2.app.api.rocketty.ranking.req;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * @author Shota.Oda 2018/02/08.
 */
@Getter
@Setter
public class GetPageDTO {
    // page
    @NotNull @Min(0)
    private Integer p;
    // size
    @NotNull @Range(min = 0, max = 100)
    private Integer s;
}
