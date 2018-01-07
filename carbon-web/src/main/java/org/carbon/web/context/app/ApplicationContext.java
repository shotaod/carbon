package org.carbon.web.context.app;

import org.carbon.web.context.Context;
import org.carbon.web.context.InstanceContainer;

/**
 * @author Shota Oda 2017/02/25.
 */
public class ApplicationContext implements Context {
    // -----------------------------------------------------
    //                                               application scope
    //                                               -------
    private static InstanceContainer instanceContainer;

    public static ApplicationContext instance = new ApplicationContext();

    private ApplicationContext() {}

    public static void initialize(InstanceContainer instanceContainer) {
        ApplicationContext.instanceContainer = instanceContainer;
    }

    public <T> T getByType(Class<T> type) {
        return instanceContainer.getByType(type);
    }
}
