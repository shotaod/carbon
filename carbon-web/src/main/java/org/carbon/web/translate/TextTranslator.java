package org.carbon.web.translate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.web.translate.dto.Text;

/**
 * @author Shota.Oda 2018/02/19.
 */
@Component
public class TextTranslator implements HttpTranslator<Text> {
    @Override
    public Class<Text> targetType() {
        return Text.class;
    }

    @Override
    public void translate(HttpServletRequest request, HttpServletResponse response, Text text) throws Throwable {
        response.setContentType("text/plain");
        response.getWriter().println(text.getText());
    }
}
