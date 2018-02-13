package org.carbon.component.meta;

import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.component.exception.ImpossibleDetermineException;

/**
 * @author Shota Oda 2018/01/11.
 */
public interface ComponentQualifier {
    boolean shouldHandle(ComponentMeta<?> meta);

    void awareDependency(ComponentMeta<?> meta, ComponentMetaSet dependency) throws ClassNotRegisteredException;

    boolean isQualified(ComponentMeta<?> meta) throws ImpossibleDetermineException;
}
