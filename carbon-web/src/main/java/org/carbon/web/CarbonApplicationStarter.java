package org.carbon.web;

import org.carbon.component.ComponentFactory;
import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentMetaSet;
import org.carbon.modular.ModuleConfigurer;
import org.carbon.modular.ModuleConfigurerResolver;
import org.carbon.modular.env.EnvironmentMapper;
import org.carbon.util.fn.Fn;
import org.carbon.util.format.ChapterAttr;
import org.carbon.web.context.InstanceContainer;
import org.carbon.web.context.app.ApplicationContext;
import org.carbon.web.def.Logo;
import org.carbon.web.exception.ApplicationStartException;
import org.carbon.web.server.EmbedServer;
import org.carbon.web.server.jetty.JettyServerBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Shota Oda 2016/10/06.
 */
public class CarbonApplicationStarter {
    private Logger logger = LoggerFactory.getLogger(CarbonApplicationStarter.class);


    // ===================================================================================
    //                                                                          Field
    //                                                                          ==========
    private Set<Class<? extends ModuleConfigurer>> moduleConfigurers = new HashSet<>();

    @SafeVarargs
    public final void addModuleConfigurers(Class<? extends ModuleConfigurer>... moduleConfigurers) {
        this.moduleConfigurers = Stream.of(moduleConfigurers).collect(Collectors.toSet());
    }

    // ===================================================================================
    //                                                                              Public
    //                                                                              ======
    public void start(Class scanBase) throws Exception {
        start(scanBase,
                () -> logger.info(ChapterAttr.get("Carbon Initialize Finished")),
                () -> logger.info(ChapterAttr.get("Carbon Start Running")));
    }

    public void start(Class scanBase, Runnable onPrepare, Runnable onStart) throws Exception {
        // prepare
        EmbedServer embedServer = Fn
                .Try(() -> prepare(scanBase))
                .CatchThrow(e -> new ApplicationStartException("Application Start up Error", e));

        onPrepare.run();

        // run
        try {
            embedServer.run();
        } catch (Exception e) {
            throw new ApplicationStartException("Application Start running Error", e);
        }

        onStart.run();

        // await
        embedServer.await();
    }

    private EmbedServer prepare(Class scanBase) {
        logger.info(new Logo().logo);
        logger.info(ChapterAttr.get("Carbon Initialize Started"));


        // resolve external module
        Set<ComponentMeta> metas = resolveModuleConfigurer();

        InstanceContainer appInstances = createInstanceContainer(scanBase, metas);

        ApplicationContext.initialize(appInstances);

        return appInstances.getByType(JettyServerBridge.class);
    }

    // ===================================================================================
    //                                                                             Private
    //                                                                             =======
    private Set<ComponentMeta> resolveModuleConfigurer() {
        ModuleConfigurerResolver moduleConfigurerResolver = new ModuleConfigurerResolver();
        return moduleConfigurerResolver.resolve(this.moduleConfigurers);
    }

    private InstanceContainer createInstanceContainer(Class scanBase, Set<ComponentMeta> metas) {
        ComponentFactory componentFactory = new ComponentFactory();
        // load component -> create component
        Set<ComponentMeta> carbonWebMetas = componentFactory.scanComponent(CarbonWebScanBase.class).stream().map(ComponentMeta::noImpl).collect(Collectors.toSet());
        Set<ComponentMeta> clientMetas = componentFactory.scanComponent(scanBase).stream().map(ComponentMeta::noImpl).collect(Collectors.toSet());

        ComponentMetaSet baseMetas = new ComponentMetaSet(metas);
        baseMetas.addAll(carbonWebMetas);
        baseMetas.addAll(clientMetas);

        ComponentMetaSet resolvedMetas = componentFactory.resolve(baseMetas);
        return new InstanceContainer(resolvedMetas.stream().collect(Collectors.toMap(ComponentMeta::getType, ComponentMeta::getInstance)));
    }
}
