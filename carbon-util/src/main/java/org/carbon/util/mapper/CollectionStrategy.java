package org.carbon.util.mapper;

import java.util.Collection;

/**
 * @author ubuntu 2017/03/08.
 */
public interface CollectionStrategy<TYPE, CTYPE extends Collection<?>> {
    CTYPE cast(TYPE object);
    CTYPE cast(CTYPE objects);
}
