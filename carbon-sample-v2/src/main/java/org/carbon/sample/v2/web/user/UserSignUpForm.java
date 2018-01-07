package org.carbon.sample.v2.web.user;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Shota Oda 2017/02/19.
 */
@Setter
@Getter
public class UserSignUpForm {
    @NotBlank
    @Length(min = 4)
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 32)
    private String password;
    private String password2;

    public boolean isIllegalPassword() {
        return password.equals(password2);
    }
}
