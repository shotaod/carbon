package org.carbon.util.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shota Oda 2018/01/09.
 */
public class MergeUtil {

    @SuppressWarnings("unchecked")
    public static Map merge(Map base, Map apply) {
        return doMerge(new HashMap(base), apply);
    }

    public static Map merge(Collection<Map> maps) {
        return maps.stream().reduce(MergeUtil::merge).orElseGet(HashMap::new);
    }

    @SuppressWarnings("unchecked")
    protected static Map doMerge(Map base, Map apply) {
        for (Object key : apply.keySet()) {
            Object baseVal = base.get(key);
            Object applyVal = apply.get(key);
            if (baseVal instanceof Map && applyVal instanceof Map) {
                base.put(key, doMerge((Map) baseVal, (Map) applyVal));
                continue;
            }
            if (baseVal instanceof Collection && applyVal instanceof Collection) {
                ((Collection) baseVal).addAll((Collection) applyVal);
                continue;
            }
            base.put(key, applyVal);
        }
        return base;
    }
}
