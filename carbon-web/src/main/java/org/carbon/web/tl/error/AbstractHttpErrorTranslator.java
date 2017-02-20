package org.carbon.web.tl.error;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;


/**
 * @author Shota Oda 2017/02/19.
 */
public abstract class AbstractHttpErrorTranslator implements HttpErrorTranslator{
    @Override
    public boolean tryTranslate(Throwable throwable, HttpServletResponse response) {
        try {
            return doTranslate(throwable, response);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("", e);
            return false;
        }
    }
    abstract protected boolean doTranslate(Throwable throwable, HttpServletResponse response) throws Exception;
}
