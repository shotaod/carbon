package org.carbon.web.tl.error;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.response.processor.HtmlProcessor;
import org.carbon.web.exception.ActionNotFoundException;
import static java.util.AbstractMap.SimpleEntry;
import static java.util.Map.Entry;

/**
 *
 * @author Shota Oda 2016/10/18.
 */
public class MinimumHtmlTranslator extends AbstractHttpErrorTranslator{
    private interface ThrowableErrorResponseConsumer {
        void consume(Throwable throwable, HttpServletResponse response) throws Exception;
    }

    protected List<Entry<Class<? extends Throwable>, ThrowableErrorResponseConsumer>> rule;
    protected HtmlProcessor htmlProcessor;
    protected String notFoundPage;
    protected String internalServerErrorPage;

    public MinimumHtmlTranslator(HtmlProcessor htmlProcessor) {
        this.rule = new ArrayList<>();
        this.htmlProcessor = htmlProcessor;

        rule.add(new SimpleEntry<>(ActionNotFoundException.class, (e, resp) -> {
            int status = HttpServletResponse.SC_NOT_FOUND;
            if (notFoundPage != null) {
                writeCustom(status, notFoundPage, resp);
            }
            else writeDefault(status, "Not Found", resp);
        }));

        rule.add(new SimpleEntry<>(Exception.class, (e, resp) -> {
            int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            if (internalServerErrorPage != null) {
                writeCustom(status, internalServerErrorPage, resp);
            }
            else writeDefault(status, "Internal Server Error", resp);
        }));
    }

    public void setNotFoundPage(String notFoundPage) {
        this.notFoundPage = notFoundPage;
    }
    public void setInternalServerErrorPage(String internalServerErrorPage) {
        this.internalServerErrorPage = internalServerErrorPage;
    }

    @Override
    protected boolean doTranslate(Throwable throwable, HttpServletResponse response) throws Exception {
        for (Entry<Class<? extends Throwable>, ThrowableErrorResponseConsumer> errorConsumer : rule) {
            if (errorConsumer.getKey().isAssignableFrom(throwable.getClass())) {
                errorConsumer.getValue().consume(throwable, response);
                return true;
            }
        }
        return false;
    }

    private void writeDefault(int status, String message, HttpServletResponse response) throws IOException {
        setCommon(status, response);
        PrintWriter writer = response.getWriter();
        writer.println("<h1>" + status + "</h1>");
        writer.println("<h3>" + message + "</h3>");
    }

    private void writeCustom(int status, String path, HttpServletResponse response) {
        htmlProcessor.init(new HtmlResponse(path)).process(response);
        setCommon(status, response);
    }

    private void setCommon(int status, HttpServletResponse response) {
        response.setStatus(status);
        response.setContentType("text/html;charset=UTF-8");
    }
}
