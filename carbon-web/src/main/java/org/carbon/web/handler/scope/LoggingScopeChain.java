package org.carbon.web.handler.scope;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.util.SimpleKeyValue;
import org.carbon.util.format.BoxedTitleMessage;
import org.carbon.util.format.ChapterAttr;
import org.carbon.util.format.StringLineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class LoggingScopeChain extends ScopeChain {

    private static Logger logger = LoggerFactory.getLogger(LoggingScopeChain.class);
    private static final ThreadLocal<StringLineBuilder> logMessage = ThreadLocal.withInitial(() -> null);
    private Boolean isDebugEnable = logger.isDebugEnabled();

    @Override
    protected void in(HttpServletRequest request, HttpServletResponse response) {
        if (!isDebugEnable) return;

        List<SimpleKeyValue<String, ?>> requestInfo = new ArrayList<>();
        requestInfo.add(new SimpleKeyValue<>("uri", request.getPathInfo()));
        requestInfo.add(new SimpleKeyValue<>("method", request.getMethod()));
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String hName = headerNames.nextElement();
            requestInfo.add(new SimpleKeyValue<>("(RH)" + hName, request.getHeader(hName)));
        }

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String pName = parameterNames.nextElement();
            requestInfo.add(new SimpleKeyValue<>("(RP)" + pName, request.getParameter(pName)));
        }
        logMessage.set(ChapterAttr.getBuilder("Request").appendLine(BoxedTitleMessage.produceLeft(requestInfo)));
    }

    @Override
    protected void out(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!isDebugEnable) return;

            StringLineBuilder requestSb = logMessage.get();
            List<SimpleKeyValue<String, ?>> headers = response.getHeaderNames().stream()
                    .map(name -> new SimpleKeyValue<>(name, response.getHeader(name)))
                    .collect(Collectors.toList());
            String resp = ChapterAttr.getBuilder("Response [" + response.getStatus() + "]").appendLine(BoxedTitleMessage.produceLeft(headers)).toString();
            logger.debug(requestSb.appendLine(resp).toString());
        } finally {
            logMessage.remove();
        }
    }
}
