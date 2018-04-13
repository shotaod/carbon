package org.carbon.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.web.translate.EntryTranslator;

/**
 * @author Shota.Oda 2018/02/16.
 */
@Component
public class ResponseTranslatorChain extends HandlerChain {

    @Assemble
    private EntryTranslator entryTranslator;

    @Override
    protected void chain(HttpServletRequest request, HttpServletResponse response) {
        entryTranslator.translate(() -> super.chain(request, response), request, response);
    }
}
