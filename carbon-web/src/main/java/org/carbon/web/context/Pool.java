package org.carbon.web.context;

import java.util.Optional;

/**
 * Pool
 *
 * @author Shota Oda 2017/02/25.
 */
public interface Pool {
    <T> Optional<T> getByType(Class<T> type);
}
