package org.carbon.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;

/**
 * @author Shota Oda 2016/10/22.
 */
@Component
public class CharacterEncodingChain extends HandlerChain {

    private static final String encode = "utf-8";

    @Override
    protected void chain(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(encode);
        super.chain(request, response);
    }
}
