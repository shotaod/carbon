package org.carbon.modular;

import java.util.Map;
import java.util.Set;

import org.carbon.util.mapper.ConfigHolder;

/**
 * @author Shota Oda 2017/02/09.
 */
public interface ModuleConfigurer {
    Set<Class> registerClass(Class scanBase, ConfigHolder configHolder);
    Map<Class, Object> registerObject(Class scanBase, ConfigHolder configHolder);
}
