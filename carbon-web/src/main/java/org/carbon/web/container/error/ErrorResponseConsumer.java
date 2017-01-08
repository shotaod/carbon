package org.carbon.web.container.error;

import javax.servlet.http.HttpServletResponse;
import java.util.function.BiConsumer;

/**
 * @author Shota Oda 2016/10/18.
 */
public interface ErrorResponseConsumer extends BiConsumer<Throwable, HttpServletResponse> {
}
