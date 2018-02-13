package org.carbon.component.meta;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.component.exception.ImpossibleDetermineException;

/**
 * @author Shota Oda 2018/01/01.
 */
public class ComponentMetaSet extends AbstractSet<ComponentMeta> implements Set<ComponentMeta>, ComponentQualifier {

    public static class Collectors {
        public static Collector<ComponentMeta, ?, ComponentMetaSet> toSet() {
            return java.util.stream.Collectors.toCollection(ComponentMetaSet::new);
        }
    }

    private Map<ComponentMeta, ComponentMeta> metaMap;
    private Set<ComponentQualifier> qualifiers;

    public ComponentMetaSet() {
        this(new HashSet<>(), new HashSet<>());
    }

    public ComponentMetaSet(ComponentMetaSet componentMetaSet) {
        this(componentMetaSet.metaMap.keySet(), componentMetaSet.qualifiers);
    }

    private ComponentMetaSet(Collection<ComponentMeta> metas, Set<ComponentQualifier> qualifiers) {
        this.metaMap = new HashMap<>(metas.stream().collect(java.util.stream.Collectors.toMap(Function.identity(), Function.identity())));
        this.metaMap.forEach((k, v) -> k.setParent(this));
        this.qualifiers = qualifiers != null ? qualifiers : new HashSet<>();
    }

    public void addQualifier(ComponentQualifier qualifier) {
        Class<? extends ComponentQualifier> extClass = qualifier.getClass();
        boolean contains = qualifiers.stream().anyMatch(ext -> ext.getClass().equals(extClass));
        if (!contains) qualifiers.add(qualifier);
    }

    @SuppressWarnings("unchecked")
    public <T> ComponentMeta<T> get(Class<T> type) {
        return (ComponentMeta<T>) metaMap.get(ComponentMeta.noImpl(type));
    }

    // -----------------------------------------------------
    //                                               assign
    //                                               -------
    public ComponentMetaSet assign(ComponentMetaSet other) {
        ComponentMetaSet assignBase = new ComponentMetaSet(this);
        assignBase.mergeMetaMap(other.metaMap);
        assignBase.mergeQualifiers(other.qualifiers);
        return assignBase;
    }

    private void mergeMetaMap(Map<ComponentMeta, ComponentMeta> other) {
        other.keySet().forEach(otherMeta -> {
            ComponentMeta selfMeta = metaMap.get(otherMeta);
            if (selfMeta != null) {
                selfMeta.merge(otherMeta);
            } else {
                metaMap.put(otherMeta, otherMeta);
            }
        });
    }

    private void mergeQualifiers(Set<ComponentQualifier> other) {
        other.forEach(this::addQualifier);
    }

    // ===================================================================================
    //                                                                   ComponentMeta Ext
    //                                                                          ==========
    @Override
    public boolean shouldHandle(ComponentMeta meta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isQualified(ComponentMeta meta) throws ImpossibleDetermineException {
        for (ComponentQualifier extension : qualifiers) {
            if (extension.shouldHandle(meta)) {
                return extension.isQualified(meta);
            }
        }
        return true;
    }

    @Override
    public void awareDependency(ComponentMeta meta, ComponentMetaSet dependency) throws ClassNotRegisteredException {
        for (ComponentQualifier extension : qualifiers) {
            if (extension.shouldHandle(meta)) {
                extension.awareDependency(meta, dependency);
            }
        }
    }

    // ===================================================================================
    //                                                                      Delegate (Set)
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
        componentMeta.setParent(this);
        metaMap.put(componentMeta, componentMeta);
        return true;
    }

    // ===================================================================================
    //                                                                        Basic Object
    //                                                                          ==========

    @Override
    public String toString() {
        return metaMap.keySet()
                .stream()
                .map(meta -> "-" + meta.toString())
                .collect(java.util.stream.Collectors.joining("\n", "metasets", ""));
    }
}
