package org.carbon.sample.prop;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.sample.prop.sub.SubProp1;
import org.carbon.sample.prop.sub.SubProp2;

/**
 * @author Shota Oda 2016/10/03.
 */
@Component
public class RootProp {
    @Assemble
    private SubProp1 subProp1;
    @Assemble
    private SubProp2 subProp2;
    @Assemble
    private BrotherProp broProp;
    @Assemble
    private SisterProp sisProp;
}
