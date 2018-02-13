package org.carbon.sample.v2.web.api.rocketty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * @author garden 2018/02/08.
 */
@Getter
@Setter
public class PageParamDTO {
    // page
    @NotNull @Min(0)
    private Integer p;
    // size
    @NotNull @Range(min = 0, max = 100)
    private Integer s;
}
