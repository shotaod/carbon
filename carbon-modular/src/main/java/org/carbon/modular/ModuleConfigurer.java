package org.carbon.modular;

import org.carbon.util.mapper.PropertyMapper;

/**
 * @author Shota Oda 2017/02/09.
 */
public interface ModuleConfigurer {
    ModuleConfigurationResult configure(Class scanBase, PropertyMapper propertyMapper);
}
