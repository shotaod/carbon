package org.carbon.web.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shota Oda 2016/10/08.
 */
public class InstanceContainer {
    private Map<Class, Object> instances;

    public InstanceContainer() {
        this.instances = new HashMap<>();
    }

    public InstanceContainer(Map<Class, Object> instances) {
        this.instances = instances;
    }

    public Map<Class, Object> getInstances() {
        return instances;
    }

    @SuppressWarnings("unchecked")
    public void set(Object object, Class typeAs) {
        boolean assignableFrom = typeAs.isAssignableFrom(object.getClass());
        if (!assignableFrom) throw new ClassCastException();
        this.instances.put(typeAs, object);
    }

    public void set(Object object) {
        this.instances.put(object.getClass(), object);
    }

    public void remove(Class type) {
        this.instances.remove(type);
    }

    @SuppressWarnings("unchecked")
    public <T> T getByType(Class<T> type) {
        return (T)this.instances.get(type);
    }
}
