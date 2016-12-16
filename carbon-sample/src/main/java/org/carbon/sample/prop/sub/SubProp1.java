package org.carbon.sample.prop.sub;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.prop.sub.supersub.SuperSubProp1;

/**
 * @author Shota Oda 2016/10/09.
 */
@Component
public class SubProp1 {
	@Inject
	private SuperSubProp1 ssProp1;
}
