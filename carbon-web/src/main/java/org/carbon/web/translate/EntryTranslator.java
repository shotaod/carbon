package org.carbon.web.translate;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.container.ActionResult;
import org.carbon.web.context.request.RequestPool;
import org.carbon.web.exception.WrappedException;
import org.carbon.web.translate.action.ActionDelegateTranslator;
import org.carbon.web.translate.decorate.HttpDecorator;
import org.carbon.web.translate.error.ErrorDelegateTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota.Oda 2018/02/18.
 */
@Component
public class EntryTranslator {
    private static final Logger logger = LoggerFactory.getLogger(EntryTranslator.class);

    public interface ThrowableFunction {
        void execute() throws Throwable;
    }

    @Assemble
    private RequestPool requestContext;
    @Assemble
    private ActionDelegateTranslator actionDelegateTranslator;
    @Assemble
    private ErrorDelegateTranslator errorDelegateTranslator;

    @Assemble
    private List<HttpDecorator> httpDecorators;

    public void translate(ThrowableFunction beforeTranslate, HttpServletRequest request, HttpServletResponse response) {
        try {
            beforeTranslate.execute();
            ActionResult actionResult = requestContext.getByType(ActionResult.class)
                    .orElseThrow(() -> new IllegalStateException("ActionResult not found"));
            actionDelegateTranslator.translate(request, response, actionResult);
            httpDecorators.forEach(decorator -> decorator.decorate(response));
        } catch (Throwable throwable) {
            if (throwable instanceof WrappedException) {
                throwable = ((WrappedException) throwable).get();
            }
            logger.debug("[Translate] handle exception", throwable);
            errorDelegateTranslator.translate(request, response, throwable);
        }
    }
}
