package org.dabuntu.sample.prop;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.prop.sub.SubProp1;
import org.dabuntu.sample.prop.sub.SubProp2;

/**
 * @author ubuntu 2016/10/03.
 */
@Component
public class RootProp {
	@Inject
	private SubProp1 subProp1;
	@Inject
	private SubProp2 subProp2;
	@Inject
	private BrotherProp broProp;
	@Inject
	private SisterProp sisProp;
}
