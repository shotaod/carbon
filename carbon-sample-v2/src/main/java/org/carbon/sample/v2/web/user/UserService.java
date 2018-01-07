package org.carbon.sample.v2.web.user;

import java.util.Optional;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.sample.v2.ext.jooq.tables.daos.UserDao;
import org.carbon.sample.v2.ext.jooq.tables.pojos.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Shota Oda 2017/02/13.
 */
@Component
public class UserService {
    @Inject
    private UserDao userDao;

    @Transactional
    public void registerUser(UserSignUpForm form) {
        User user = new User(null, form.getEmail(), form.getUsername(), BCrypt.hashpw(form.getPassword(), BCrypt.gensalt()));
        userDao.insert(user);
    }

    public Optional<User> findByEmail(String email) {
        return userDao.fetchByEmail(email).stream().findFirst();
    }
}
