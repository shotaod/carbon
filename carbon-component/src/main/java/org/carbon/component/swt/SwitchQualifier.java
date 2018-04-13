package org.carbon.component.swt;

import java.util.HashMap;
import java.util.Map;

import org.carbon.component.annotation.Switch;
import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentQualifier;
import org.carbon.component.meta.ComponentMetaSet;

/**
 * @author Shota Oda 2018/01/11.
 */
public class SwitchQualifier implements ComponentQualifier {
    private Map<ComponentMeta, Switcher> switchers;

    public SwitchQualifier() {
        switchers = new HashMap<>();
    }

    @Override
    public boolean shouldHandle(ComponentMeta<?> meta) {
        return meta.annotatedBy(Switch.class);
    }

    @Override
    public void awareDependency(ComponentMeta<?> meta, ComponentMetaSet dependency) throws ClassNotRegisteredException {
        Switch annotation  = meta.getAnnotation(Switch.class);
        Class<? extends Switcher> switcherClass = annotation.value();
        ComponentMeta<? extends Switcher> switcher = dependency.get(switcherClass);
        if (switcher == null) {
            throw new ClassNotRegisteredException(switcherClass.getClass());
        }
        switchers.put(meta, switcher.getInstance());
    }

    @Override
    public boolean isQualified(ComponentMeta<?> meta) {
        return switchers.get(meta).on(meta.getInstance().getClass());
    }
}
