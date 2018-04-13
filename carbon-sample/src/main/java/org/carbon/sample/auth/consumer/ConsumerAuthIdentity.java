package org.carbon.sample.auth.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.carbon.authentication.AuthIdentity;
import org.carbon.sample.ext.jooq.tables.pojos.Student;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Shota Oda 2016/11/23.
 */
@Getter
@AllArgsConstructor
public class ConsumerAuthIdentity implements AuthIdentity {

    private Student student;

    @Override
    public String identity() {
        return student.getUsername();
    }

    @Override
    public boolean confirm(String plainSecret) {
        return BCrypt.checkpw(plainSecret, student.getPassword());
    }
}
