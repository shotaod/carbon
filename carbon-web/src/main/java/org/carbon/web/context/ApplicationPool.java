package org.carbon.web.context;

import org.carbon.web.context.session.SessionContainer;

import java.util.Map;

/**
 * @author Shota Oda 2016/10/05.
 */
public class ApplicationPool {
    // ===================================================================================
    //                                                                                Pool
    //                                                                                ====

    // -----------------------------------------------------
    //                                               application scoped
    //                                               -------
    // base pool
    private static InstanceContainer appPool;
    // separate with app pool for search action easily

    // -----------------------------------------------------
    //                                               request scoped
    //                                               -------
    public static ApplicationPool instance = new ApplicationPool();

    private ApplicationPool() {}

    public void setPool(Map<Class, Object> instances) {
        ApplicationPool.appPool = new InstanceContainer(instances);
    }
    public void setPool(SecurityContainer securityPool) {
        ApplicationPool.appPool.set(securityPool);
    }
    public void setPool(ActionDefinitionContainer actionDefinitionContainer) {
        ApplicationPool.appPool.set(actionDefinitionContainer);
    }

    public InstanceContainer getAppPool() {
        return appPool;
    }
}
