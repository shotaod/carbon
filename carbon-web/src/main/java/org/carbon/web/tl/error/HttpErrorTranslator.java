package org.carbon.web.tl.error;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2017/02/19.
 */
public interface HttpErrorTranslator {
    boolean tryTranslate(Throwable throwable, HttpServletResponse response);
}
