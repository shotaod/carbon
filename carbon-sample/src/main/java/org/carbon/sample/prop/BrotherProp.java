package org.carbon.sample.prop;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.sample.prop.sub.SubProp1;

/**
 * @author Shota Oda 2016/10/09.
 */
@Component
public class BrotherProp {
    @Assemble
    private SubProp1 subProp1;
}
