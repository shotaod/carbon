package org.carbon.web.translate.error;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.AfterAssemble;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.translate.HttpTranslator;
import org.carbon.web.translate.TextTranslator;
import org.carbon.web.translate.dto.ErrorTranslatableResult;
import org.carbon.web.translate.dto.Text;
import org.carbon.web.translate.dto.Translatable;
import org.carbon.web.translate.error.action.InternalServerNoWayAction;
import org.carbon.web.translate.error.action.ThrowableHandleAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota.Oda 2018/02/15.
 */
@Component
public class ErrorDelegateTranslator implements HttpTranslator<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(ErrorDelegateTranslator.class);

    @Assemble
    private List<ThrowableHandleAction<?>> throwableHandleActions;
    @Assemble
    private List<HttpTranslator> httpTranslators;

    // for the last resort
    @Assemble
    private InternalServerNoWayAction noWayAction;
    @Assemble
    private TextTranslator textTranslator;

    @AfterAssemble
    public void afterInject() {
        throwableHandleActions.sort(Comparator.naturalOrder());
    }

    @Override
    public Class<Throwable> targetType() {
        return Throwable.class;
    }

    @Override
    public void translate(HttpServletRequest request, HttpServletResponse response, Throwable throwable) {
        try {
            doTranslate(request, response, throwable);
        } catch (IOException ioe) {
            logger.error("Fail to write response.", ioe);
        } catch (Throwable t) {
            // mmm... translate anyway
            try {
                logger.error("Fail to translate throwable. try translate as internal server error", t);
                translateAsInternalServerError(request, response, throwable);
            } catch (Throwable ohmygod) {
                logger.error("Unexpected error occurred. Fail to respond.", ohmygod);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void doTranslate(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws Throwable {
        ErrorTranslatableResult<?> translatableException = null;
        for (ThrowableHandleAction<?> translator : throwableHandleActions) {
            if (translator.supported(throwable)) {
                translatableException = ((ThrowableHandleAction<Throwable>) translator).execute(request, throwable);
                break;
            }
        }

        if (translatableException != null) {
            logger.debug("[Translate] throwable: {} is translated to [code: {}, result: {}]",
                    throwable.getClass(),
                    translatableException.getCode(),
                    translatableException.getTranslatable().getClass());
            // write response
            // - set status
            response.setStatus(translatableException.getCode());
            // - translate to body
            Translatable translatable = translatableException.getTranslatable();
            for (HttpTranslator<?> translator : httpTranslators) {
                if (translator.supported(translatable)) {
                    ((HttpTranslator<Object>) translator).translate(request, response, translatable);
                    return;
                }
            }
        }

        // mmm... the last resort
        // unexpected exception occurred, so we have no choice but to translate as internal server error as text
        if (translatableException == null) {
            logger.error("[Translate] Unknown throwable is thrown. No choice but to translate as internal server error", throwable);
        } else {
            logger.error("[Translate] Missing translator for {}. No choice but to translate as internal server error", translatableException.getTranslatable().getClass());
        }
        translateAsInternalServerError(request, response, throwable);
    }

    private void translateAsInternalServerError(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws Throwable {
        ErrorTranslatableResult<Text> internalServerErrorTextResult = noWayAction.execute(request, throwable);
        response.setStatus(internalServerErrorTextResult.getCode());
        textTranslator.translate(request, response, internalServerErrorTextResult.getTranslatable());
    }
}
