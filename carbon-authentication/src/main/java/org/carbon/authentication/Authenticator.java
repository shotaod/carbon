package org.carbon.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.authentication.exception.AuthenticationException;

/**
 * @author Shota.Oda 2018/02/25.
 */
public interface Authenticator {
    void authenticate(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException;
}
