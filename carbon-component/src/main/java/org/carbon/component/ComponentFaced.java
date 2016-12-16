package org.carbon.component;

import org.carbon.component.generator.InstanceGenerator;
import org.carbon.component.inject.InstanceInjector;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.generator.CallbackConfiguration;
import org.carbon.component.scan.TargetBaseScanner;

import java.util.Arrays;
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

//	/**
//	 * for Simple use
//	 * @param scanBase
//	 * @param acceptAnnotations
//	 * @return
//	 * @throws Exception
//	 */
//	public Map<Class, Object> inject(Class scanBase, List<Class> acceptAnnotations) throws Exception {
//
//		// scan all classes under or flat to scanBase
//		TargetBaseScanner scanner = new TargetBaseScanner();
//		List<Class> classes = scanner.inject(scanBase);
//
//		// create Object
//		InstanceGenerator instanceGenerator = new InstanceGenerator();
//		instanceGenerator.setCallbacks(Collections.singletonList(new InOutLoggingInterceptor()));
//		Map<Class, Object> instanceMap = instanceGenerator.generate(classes);
//
//		// inject Dependency
//		InstanceInjector instanceInjector = new InstanceInjector(instanceMap);
//		instanceInjector.setTargets(acceptAnnotations);
//		Map<Class, Object> instanceResolvedMap = instanceInjector.inject();
//
//		return instanceResolvedMap;
//	}

	// ===================================================================================
	//                                                                      For Manual Use
	//                                                                      ==============

	public Set<Class> scan(Class scanBase) throws Exception{
		return scanner.scan(scanBase).stream().collect(Collectors.toSet());
	}

	public Set<Class> scan(Class scanBase, Set<Class> whiteAnnotationList) throws Exception {
		return scanner.scan(scanBase).stream()
			.filter(clazz -> {
				return Arrays.stream(clazz.getAnnotations())
						.anyMatch(annotation -> {
							return whiteAnnotationList.contains(annotation.annotationType());
						});
			}).collect(Collectors.toSet());
	}

	/**
	 *
	 * @param classes
	 * @param dependency
	 * @param configuration
	 * @return
	 * @throws Exception
	 */
	public Map<Class, Object> generate(Set<Class> classes, Map<Class,Object> dependency, CallbackConfiguration configuration) throws Exception{
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
