package org.carbon.sample.prop;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.prop.sub.SubProp1;
import org.carbon.sample.prop.sub.SubProp2;

/**
 * @author Shota Oda 2016/10/03.
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
