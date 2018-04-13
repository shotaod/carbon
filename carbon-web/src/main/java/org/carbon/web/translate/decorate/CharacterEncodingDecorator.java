package org.carbon.web.translate.decorate;

import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;

/**
 * @author Shota.Oda 2018/02/18.
 */
@Component
public class CharacterEncodingDecorator implements HttpDecorator {
    @Override
    public void decorate(HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
    }
}
