package org.dabuntu.web.annotation;

import org.dabuntu.web.lifecycle.LifeCycle;

/**
 * @author ubuntu 2016/11/15.
 */
public @interface AppLifeCycle {
	LifeCycle value();
}
