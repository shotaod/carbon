package org.carbon.web.translate.error.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.carbon.component.annotation.Component;
import org.carbon.web.translate.dto.ErrorTranslatableResult;
import org.carbon.web.translate.dto.Text;

/**
 * @author Shota.Oda 2018/02/19.
 */
@Component
public class InternalServerNoWayAction implements ThrowableHandleAction<Throwable> {

    @Override
    public Integer priority() {
        return LAST_RESORT_PRIORITY;
    }

    @Override
    public boolean supported(Throwable throwable) {
        return true;
    }

    @Override
    public ErrorTranslatableResult<Text> execute(HttpServletRequest request, Throwable throwable) {
        boolean notEmpty = StringUtils.isNotEmpty(throwable.getMessage());
        String message = notEmpty ? throwable.getMessage() : "";
        return new ErrorTranslatableResult<>(
                500,
                new Text("internal server error \n" + message)
        );
    }
}
