package org.carbon.sample.v2.app.api.rocketty.user;

import javax.validation.constraints.AssertTrue;

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
    private static final int LENGTH = 10;
    @NotBlank
    @Length(min = 1)
    private String displayName;

    @SuppressWarnings("unused")
    @AssertTrue(message = "displayName must be between 1 and " + LENGTH)
    private boolean isValidDisplayNameLength() {
        return displayName.codePointCount(0, displayName.length()) <= LENGTH;
    }
}
