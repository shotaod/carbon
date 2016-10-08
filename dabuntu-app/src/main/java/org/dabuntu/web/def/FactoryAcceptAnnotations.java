package org.dabuntu.web.def;

import org.dabuntu.component.instanceFactory.annotation.Component;
import org.dabuntu.web.annotation.Controller;

import java.util.Arrays;
import java.util.List;

/**
 * @author ubuntu 2016/10/07.
 */
public class FactoryAcceptAnnotations {
	public static List<Class> get() {
		return Arrays.asList(
			Component.class,
			Controller.class
		);
	}
}
