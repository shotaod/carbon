package org.carbon.modular.qua;

import java.lang.reflect.Field;

import org.carbon.component.annotation.Inject;
import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.component.exception.ImpossibleDetermineException;
import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentMetaSet;
import org.carbon.component.meta.ComponentQualifier;
import org.carbon.modular.annotation.Property;
import org.carbon.modular.conf.PropertyConfiguration;

/**
 * @author Shota Oda 2018/01/11.
 */
public class PropertyInjectQualifier implements ComponentQualifier {

    private ComponentMeta<PropertyConfiguration> propertyConfiguration;

    @Override
    public boolean shouldHandle(ComponentMeta<?> meta) {
        return true;
    }

    @Override
    public void awareDependency(ComponentMeta<?> meta, ComponentMetaSet dependency) throws ClassNotRegisteredException {
        ComponentMeta<PropertyConfiguration> propertyConfigurationComponentMeta = dependency.get(PropertyConfiguration.class);
        if (propertyConfigurationComponentMeta == null)
            throw new ClassNotRegisteredException(PropertyConfiguration.class);

        propertyConfiguration = propertyConfigurationComponentMeta;
    }

    @Override
    public boolean isQualified(ComponentMeta<?> meta) throws ImpossibleDetermineException {
        for (Field field : meta.getPrivateField()) {
            if (isPropertyInjected(field)) {
                if (!propertyConfiguration.getInstance().isReady()) {
                    throw new ImpossibleDetermineException(field.getType());
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    private boolean isPropertyInjected(Field field) {
        boolean isInject = field.isAnnotationPresent(Inject.class);
        boolean isProperty = field.getType().isAnnotationPresent(Property.class);
        return isInject && isProperty;
    }
}
