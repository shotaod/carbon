package org.carbon.web.context.app;

import java.util.Optional;

import org.carbon.web.context.Pool;
import org.carbon.web.context.InstanceContainer;

/**
 * @author Shota Oda 2017/02/25.
 */
public class ApplicationPool implements Pool {
    // -----------------------------------------------------
    //                                               application scope
    //                                               -------
    private static InstanceContainer instanceContainer;

    public static ApplicationPool instance = new ApplicationPool();

    private ApplicationPool() {}

    public static void initialize(InstanceContainer instanceContainer) {
        ApplicationPool.instanceContainer = instanceContainer;
    }

    public <T> Optional<T> getByType(Class<T> type) {
        return Optional.ofNullable(instanceContainer.getByType(type));
    }
}
