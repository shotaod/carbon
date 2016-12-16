package org.carbon.sample.prop;

import org.carbon.sample.prop.sub.SubProp1;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;

/**
 * @author ubuntu 2016/10/09.
 */
@Component
public class BrotherProp {
	@Inject
	private SubProp1 subProp1;
}
