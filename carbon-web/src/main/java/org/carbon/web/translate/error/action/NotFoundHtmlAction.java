package org.carbon.web.translate.error.action;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.AfterAssemble;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.conf.WebProperty;
import org.carbon.web.exception.ActionNotFoundException;
import org.carbon.web.translate.dto.ErrorTranslatableResult;
import org.carbon.web.translate.dto.Html;

/**
 * @author Shota.Oda 2018/02/19.
 */
@Component
public class NotFoundHtmlAction implements ThrowableHandleAction<ActionNotFoundException> {

    @Assemble
    private WebProperty webProperty;

    private Html notFoundHtml;

    @AfterAssemble
    public void afterAssemble() {
        notFoundHtml = Optional.ofNullable(webProperty.getErrorPage())
                .map(WebProperty.ErrorPage::getNotFound)
                .map(Html::new)
                .orElseGet(() -> {
                    Html html = new Html("org/carbon/web/page/not_found");
                    html.setDirectory("");
                    return html;
                });
    }

    @Override
    public Integer priority() {
        return LOW_PRIORITY;
    }

    @Override
    public boolean supported(Throwable throwable) {
        return throwable instanceof ActionNotFoundException;
    }

    @Override
    public ErrorTranslatableResult<Html> execute(HttpServletRequest request, ActionNotFoundException throwable) {
        return new ErrorTranslatableResult<>(
                HttpServletResponse.SC_NOT_FOUND,
                notFoundHtml);
    }
}
