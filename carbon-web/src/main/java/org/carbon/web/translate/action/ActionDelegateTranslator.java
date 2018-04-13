package org.carbon.web.translate.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.container.ActionResult;
import org.carbon.web.translate.HttpTranslator;

/**
 * @author Shota Oda 2016/10/05.
 */
@Component
public class ActionDelegateTranslator implements HttpTranslator<ActionResult> {

    @Assemble
    private List<HttpTranslator> translators;

    @Override
    public Class<ActionResult> targetType() {
        return ActionResult.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void translate(HttpServletRequest request, HttpServletResponse response, ActionResult actionResult) throws Throwable {
        if (actionResult.handled()) return;

        Object result = actionResult.getResult();
        for (HttpTranslator<?> translator : translators) {
            if (translator.supported(result)) {
                ((HttpTranslator<Object>) translator).translate(request, response, result);
                return;
            }
        }

        throw new UnsupportedOperationException(String.format(
                "Not found HttpTranslator for Class<%s>",
                result.getClass()
        ));
    }
}
