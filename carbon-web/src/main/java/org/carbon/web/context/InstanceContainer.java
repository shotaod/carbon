package org.carbon.web.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.carbon.component.exception.ImpossibleDetermineException;
import org.carbon.web.exception.WrappedException;

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
        T t = (T) this.instances.get(type);
        if (t != null) {
            return t;
        }

        List<T> candidate = (List<T>) this.instances.entrySet().stream()
                .filter(e -> type.isAssignableFrom(e.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        if (candidate.isEmpty()) {
            return null;
        }
        if (candidate.size() > 1) {
            throw WrappedException.wrap(new ImpossibleDetermineException(type));
        }

        return candidate.get(0);
    }
}
