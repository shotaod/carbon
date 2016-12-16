package org.carbon.sample.prop;

import org.carbon.sample.prop.sub.supersub.SuperSubProp1;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;

/**
 * @author Shota Oda 2016/10/09.
 */
@Component
public class SisterProp {
	@Inject
	private BrotherProp broProp;
	@Inject
	private SuperSubProp1 ssProp1;
}
