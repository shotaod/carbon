package org.carbon.sample.v2.app.api.rocketty.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author garden 2018/03/25.
 */
@Getter
@Setter
public class PostUserDTO {
    @NotBlank
    @Length(max = 255)
    private String displayName;
}
