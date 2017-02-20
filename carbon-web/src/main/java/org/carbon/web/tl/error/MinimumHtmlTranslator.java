package org.carbon.web.tl.error;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.response.processor.HtmlProcessor;
import org.carbon.web.exception.ActionInvokeException;
import org.carbon.web.exception.ActionNotFoundException;

/**
 * @author Shota Oda 2016/10/18.
 */
public class MinimumHtmlTranslator extends AbstractHttpErrorTranslator{
    private interface ThrowableErrorResponseConsumer {
        void consume(Throwable throwable, HttpServletResponse response) throws Exception;
    }

    protected Map<Class<? extends Throwable>, ThrowableErrorResponseConsumer> rule;
    protected HtmlProcessor htmlProcessor;
    protected String notFoundPage;
    protected String internalServerErrorPage;

    public MinimumHtmlTranslator(HtmlProcessor htmlProcessor) {
        this.rule = new HashMap<>();
        this.htmlProcessor = htmlProcessor;

        rule.put(ActionNotFoundException.class, (e, resp) -> {
            int status = HttpServletResponse.SC_NOT_FOUND;
            if (notFoundPage != null) {
                writeCustom(status, notFoundPage, resp);
            }
            else writeDefault(status, "Not Found", resp);
        });

        rule.put(ActionInvokeException.class, (e, resp) -> {
            int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            if (internalServerErrorPage != null) {
                writeCustom(status, internalServerErrorPage, resp);
            }
            else writeDefault(status, "Internal Server Error", resp);
        });
    }

    public void setNotFoundPage(String notFoundPage) {
        this.notFoundPage = notFoundPage;
    }
    public void setInternalServerErrorPage(String internalServerErrorPage) {
        this.internalServerErrorPage = internalServerErrorPage;
    }

    @Override
    protected boolean doTranslate(Throwable throwable, HttpServletResponse response) throws Exception {
        ThrowableErrorResponseConsumer errorConsumer = this.rule.get(throwable.getClass());
        if (errorConsumer == null) return false;

        errorConsumer.consume(throwable, response);
        return true;
    }

    private void writeDefault(int status, String message, HttpServletResponse response) throws IOException {
        setCommon(status, response);
        PrintWriter writer = response.getWriter();
        writer.println("<h1>" + status + "</h1>");
        writer.println("<h3>" + message + "</h3>");
    }

    private void writeCustom(int status, String path, HttpServletResponse response) {
        setCommon(status, response);
        htmlProcessor.with(new HtmlResponse(path)).process(response);
    }

    private void setCommon(int status, HttpServletResponse response) {
        response.setStatus(status);
        response.setContentType("text/html;charset=UTF-8");
    }
}
