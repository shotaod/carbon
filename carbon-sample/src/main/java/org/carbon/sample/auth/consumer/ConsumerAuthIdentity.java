package org.carbon.sample.auth.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.carbon.sample.tables.pojos.Student;
import org.carbon.web.auth.AuthIdentity;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Shota Oda 2016/11/23.
 */
@Data
@AllArgsConstructor
public class ConsumerAuthIdentity implements AuthIdentity{

    private Student student;

    @Override
    public String username() {
        return student.getUsername();
    }

    @Override
    public String cryptPassword() {
        return student.getPassword();
    }

    @Override
    public boolean confirm(String plainPassword) {
        return BCrypt.checkpw(plainPassword, cryptPassword());
    }
}
