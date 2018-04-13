package org.carbon.web.translate.error.action;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.AfterAssemble;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.conf.WebProperty;
import org.carbon.web.translate.dto.ErrorTranslatableResult;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.translate.dto.Translatable;

/**
 * @author Shota.Oda 2018/02/19.
 */
@Component
public class InternalServerErrorAction implements ThrowableHandleAction<Throwable> {

    @Assemble
    private WebProperty webProperty;
    private Html internalServerErrorHtml;

    @AfterAssemble
    public void afterAssemble() {
        internalServerErrorHtml = Optional.ofNullable(webProperty.getErrorPage())
                .map(WebProperty.ErrorPage::getBadRequest)
                .map(Html::new)
                .orElseGet(() -> {
                    Html html = new Html("org/carbon/web/page/internal_server_error");
                    html.setDirectory("");
                    return html;
                });
    }

    @Override
    public Integer priority() {
        return LOWEST_PRIORITY;
    }

    @Override
    public boolean supported(Throwable throwable) {
        return true;
    }

    @Override
    public ErrorTranslatableResult<? extends Translatable> execute(HttpServletRequest request, Throwable throwable) {
        return new ErrorTranslatableResult<>(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                internalServerErrorHtml);
    }
}
