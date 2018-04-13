package org.carbon.sample.prop.sub;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.sample.prop.sub.supersub.SuperSubProp1;

/**
 * @author Shota Oda 2016/10/09.
 */
@Component
public class SubProp1 {
    @Assemble
    private SuperSubProp1 ssProp1;
}
