package org.carbon.web.translate.error.action;

import javax.servlet.http.HttpServletRequest;

import org.carbon.web.translate.dto.ErrorTranslatableResult;
import org.carbon.web.translate.dto.Translatable;

/**
 * @author Shota.Oda 2018/02/19.
 */
public interface ThrowableHandleAction<IN extends Throwable> extends Comparable<ThrowableHandleAction> {

    int DEFAULT_PRIORITY = 0;
    int HIGHEST_PRIORITY = Integer.MAX_VALUE;
    int HIGH_PRIORITY = HIGHEST_PRIORITY - 1;
    int LAST_RESORT_PRIORITY = Integer.MIN_VALUE;
    int LOWEST_PRIORITY = LAST_RESORT_PRIORITY + 1;
    int LOW_PRIORITY = LOWEST_PRIORITY + 1;

    default Integer priority() {
        return DEFAULT_PRIORITY;
    }

    @Override
    default int compareTo(ThrowableHandleAction o) {
        return o.priority().compareTo(priority());
    }

    boolean supported(Throwable throwable);

    ErrorTranslatableResult<? extends Translatable> execute(HttpServletRequest request, IN throwable);
}
