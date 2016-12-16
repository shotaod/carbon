package org.carbon.sample.web.business;

import lombok.Data;
import org.carbon.sample.tables.pojos.Lecturer;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

/**
 * @author Shota Oda 2016/12/13.
 */
@Data
public class LecturerProfileForm {
    private String name;
    @Email
    private String email;
    private String prShort;
    private String prLong;

    public Lecturer toEntity() {
        Lecturer lecturer = new Lecturer();
        lecturer.setUsername(name);
        lecturer.setEmail(email);
        lecturer.setPrTextShort(prShort);
        lecturer.setPrText(prLong);
        return lecturer;
    }
}
