package org.dabuntu.component;

import org.dabuntu.component.generator.InstanceGenerator;
import org.dabuntu.component.inject.InstanceInjector;
import org.dabuntu.component.scan.TargetBaseScanner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/07.
 */
public class ComponentFaced {

	private TargetBaseScanner scanner = new TargetBaseScanner();
	private InstanceGenerator generator = new InstanceGenerator();
	private InstanceInjector  injector  = new InstanceInjector();

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
	 * choose this when Dependency is within 'classes'
	 * @param classes
	 * @return
	 * @throws Exception
	 */
	public Map<Class, Object> generate(Set<Class> classes) throws Exception{
		Map<Class, Object> instance = generator.generate(classes);
		return injector.with(instance).inject();
	}

	/**
	 * choose this when Dependency is in 'classes' over 'dependency'
	 * @param classes
	 * @param dependency
	 * @return
	 */
	public Map<Class, Object> generate(Set<Class> classes, Map<Class,Object> dependency) throws Exception{
		Map<Class, Object> instances = generator.generate(classes);
		Map<Class, Object> map = new HashMap<>();
		map.putAll(instances);
		map.putAll(dependency);
		Map<Class, Object> injected = injector.with(map).inject();
		return injected.entrySet().stream()
				.filter(e -> instances.containsKey(e.getKey()))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}
}
