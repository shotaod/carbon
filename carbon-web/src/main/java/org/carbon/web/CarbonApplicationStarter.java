package org.carbon.web;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.StopWatch;
import org.carbon.component.HandyComponentManager;
import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentMetaSet;
import org.carbon.modular.ModuleConfigurer;
import org.carbon.modular.ModuleConfigurerResolver;
import org.carbon.util.fn.Fn;
import org.carbon.util.format.ChapterAttr;
import org.carbon.web.context.InstanceContainer;
import org.carbon.web.context.app.ApplicationContext;
import org.carbon.web.def.Logo;
import org.carbon.web.exception.ApplicationStartException;
import org.carbon.web.module.ClientModuleConfigurer;
import org.carbon.web.module.WebModuleConfigurer;
import org.carbon.web.server.EmbedServer;
import org.carbon.web.server.jetty.JettyServerBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/06.
 */
public class CarbonApplicationStarter {
    private Logger logger = LoggerFactory.getLogger(CarbonApplicationStarter.class);


    // ===================================================================================
    //                                                                          Field
    //                                                                          ==========
    private Set<Class<? extends ModuleConfigurer>> moduleConfigurers;

    @SafeVarargs
    public CarbonApplicationStarter(Class<? extends ModuleConfigurer>... moduleConfigurers) {
        this.moduleConfigurers = Stream.of(moduleConfigurers).collect(Collectors.toSet());
        this.moduleConfigurers.add(WebModuleConfigurer.class);
    }

    @SafeVarargs
    public final void addModuleConfigurers(Class<? extends ModuleConfigurer>... moduleConfigurers) {
        this.moduleConfigurers.addAll(Arrays.asList(moduleConfigurers));
    }

    // ===================================================================================
    //                                                                              Public
    //                                                                              ======
    public void start(Class scanBase) throws Exception {
        StopWatch stopWatch = new StopWatch();
        start(scanBase,
                () -> {
                    stopWatch.start();
                    logger.info(ChapterAttr.get("Carbon Start Initialize (at {}ms)"), stopWatch.getTime());
                },
                () -> logger.info(ChapterAttr.get("Carbon Finish Initialize (at {}ms)"), stopWatch.getTime()),
                () -> {
                    logger.info(ChapterAttr.get("Carbon Start Running (at {}ms)"), stopWatch.getTime());
                    stopWatch.stop();
                }
        );
    }

    public void start(Class scanBase, Runnable beforePrepare, Runnable afterPrepare, Runnable afterStart) throws Exception {
        beforePrepare.run();
        // prepare
        InstanceContainer appContainer = Fn
                .Try(() -> prepare(scanBase))
                .CatchThrow(e -> new ApplicationStartException("Application Start up Error", e));

        afterPrepare.run();

        EmbedServer embedServer = appContainer.getByType(JettyServerBridge.class);
        // run
        try {
            embedServer.run();
        } catch (Exception e) {
            throw new ApplicationStartException("Application Start running Error", e);
        }

        afterStart.run();

        // await
        embedServer.await();
    }

    private InstanceContainer prepare(Class scanBase) {
        logger.info(new Logo().logo);
        logger.info(ChapterAttr.get("Carbon Initialize Started"));


        // resolve external module
        InstanceContainer appInstances = initializeContainer(scanBase);

        ApplicationContext.initialize(appInstances);

        return appInstances;
    }

    // ===================================================================================
    //                                                                             Private
    //                                                                             =======
    private InstanceContainer initializeContainer(Class scanBase) {
        ClientModuleConfigurer clientConfigurer = new ClientModuleConfigurer(scanBase);
        ModuleConfigurerResolver moduleConfigurerResolver = new ModuleConfigurerResolver(moduleConfigurers, clientConfigurer);
        ComponentMetaSet baseMetas = moduleConfigurerResolver.resolve();

        HandyComponentManager componentManager = new HandyComponentManager();

        // load component -> create component
        Set<ComponentMeta> clientMetas = componentManager.scanComponent(scanBase).stream().map(ComponentMeta::noImpl).collect(Collectors.toSet());

        baseMetas.addAll(clientMetas);

        ComponentMetaSet resolvedMetas = componentManager.resolve(baseMetas);
        return new InstanceContainer(resolvedMetas.stream().collect(Collectors.toMap(ComponentMeta::getType, ComponentMeta::getInstance)));
    }
}
