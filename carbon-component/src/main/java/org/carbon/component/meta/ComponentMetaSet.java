package org.carbon.component.meta;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2018/01/01.
 */
public class ComponentMetaSet extends AbstractSet<ComponentMeta> implements Set<ComponentMeta> {

    public static class MetaCollectors {
        public static Collector<ComponentMeta, ?, ComponentMetaSet> toSet() {
            return Collectors.toCollection(ComponentMetaSet::new);
        }
    }

    private Map<ComponentMeta, ComponentMeta> metaMap;

    public ComponentMetaSet() {
        this(new HashSet<>());
    }

    public ComponentMetaSet(ComponentMetaSet componentMetaSet) {
        this(new HashSet<>(componentMetaSet.metaMap.keySet()));
    }

    public ComponentMetaSet(Set<ComponentMeta> metaMap) {
        this.metaMap = new HashMap<>();
        this.metaMap.putAll(metaMap.stream().collect(Collectors.toMap(Function.identity(), Function.identity())));
    }

    public ComponentMetaSet assign(ComponentMetaSet other) {
        HashMap<ComponentMeta, ComponentMeta> baseMetas = new HashMap<>(this.metaMap);
        other.metaMap.keySet().forEach((otherMeta) -> {
            ComponentMeta thisMeta = this.metaMap.get(otherMeta);
            if (thisMeta != null) {
                thisMeta.merge(otherMeta);
            } else {
                baseMetas.put(otherMeta, otherMeta);
            }
        });
        return new ComponentMetaSet(baseMetas.keySet());
    }

    @SuppressWarnings("unchecked")
    public <T> ComponentMeta<T> get(Class<T> type) {
        return (ComponentMeta<T>) metaMap.get(ComponentMeta.noImpl(type));
    }

    // ===================================================================================
    //                                                                          Delegate (Set)
    //                                                                          ==========

    @Override
    public Iterator<ComponentMeta> iterator() {
        return metaMap.keySet().iterator();
    }

    @Override
    public int size() {
        return metaMap.size();
    }

    @Override
    public boolean add(ComponentMeta componentMeta) {
        boolean contain = metaMap.containsKey(componentMeta);
        if (contain) {
            return false;
        }
        metaMap.put(componentMeta, componentMeta);
        return true;
    }
}
