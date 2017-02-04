package org.carbon.component;

import org.carbon.component.annotation.Component;
import org.carbon.component.exception.PackageScanException;
import org.carbon.component.generator.InstanceGenerator;
import org.carbon.component.inject.InstanceInjector;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.generator.CallbackConfiguration;
import org.carbon.component.scan.TargetBaseScanner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/10/07.
 */
public class ComponentFaced {

	private TargetBaseScanner scanner = new TargetBaseScanner();
	private InstanceGenerator generator = new InstanceGenerator();
	private InstanceInjector injector  = new InstanceInjector();

    public Set<Class<?>> scanComponent (Class scanBase) throws PackageScanException {
        return scanner.scan(scanBase, Collections.singleton(Component.class));
    }
	public Set<Class<?>> scan(Class scanBase, Set<Class<?>> additionalScanTargets) throws PackageScanException {
        Set<Class<?>> targets = new HashSet<>();
        targets.add(Component.class);
        targets.addAll(additionalScanTargets);
        return scanner.scan(scanBase, targets);
	}

	public Map<Class, Object> generate(Set<Class<?>> classes, Map<Class,Object> dependency, CallbackConfiguration configuration) throws Exception{
		Map<Class, Object> instances = generator.generate(classes, configuration);
		instances.putAll(dependency);

		// resolve configuration component
		Map<Class, Object> configurations = instances.entrySet().stream()
			.filter(entry -> entry.getKey().isAnnotationPresent(Configuration.class))
			.collect(Collectors.toMap(
				e -> e.getKey(),
				e -> e.getValue()
			));
		Map<Class, Object> instancesSuppliedByConfiguration = generator.generateMethodComponent(injector.inject(configurations, instances));
		instances.putAll(instancesSuppliedByConfiguration);

		Map<Class, Object> injected = injector.inject(instances);
		return injected.entrySet().stream()
			.filter(e -> !dependency.containsKey(e.getKey()))
			.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}
}
