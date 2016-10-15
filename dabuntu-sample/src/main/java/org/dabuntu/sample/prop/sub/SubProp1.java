package org.dabuntu.sample.prop.sub;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.prop.sub.supersub.SuperSubProp1;

/**
 * @author ubuntu 2016/10/09.
 */
@Component
public class SubProp1 {
	@Inject
	private SuperSubProp1 ssProp1;
}
