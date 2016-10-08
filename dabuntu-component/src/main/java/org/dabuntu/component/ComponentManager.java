package org.dabuntu.component;

import org.dabuntu.component.classFactory.ClassFactory;
import org.dabuntu.component.classFactory.aop.interceptor.InOutLoggingInterceptor;
import org.dabuntu.component.instanceFactory.InstanceFactory;
import org.dabuntu.component.scan.ClassBaseScanner;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author ubuntu 2016/10/07.
 */
public class ComponentManager {
	public Map<Class, Object> initialize(Class scanBase, List<Class> acceptAnnotations) throws Exception{

		// find classes
		ClassBaseScanner scanner = new ClassBaseScanner();
		List<Class> classes = scanner.run(scanBase);

		// create Object
		ClassFactory classFactory = new ClassFactory();
		classFactory.setCallbacks(Collections.singletonList(new InOutLoggingInterceptor()));
		classFactory.setAcceptAnnotations(acceptAnnotations);
		Map<Class, Object> instanceMap = classFactory.initialize(classes);

		// inject Dependency
		InstanceFactory instanceFactory = new InstanceFactory(instanceMap);
		Map<Class, Object> instanceResolvedMap = instanceFactory.run();

		return instanceResolvedMap;
	}
}
