package org.carbon.util.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.carbon.util.exception.UndefinedValueException;

/**
 * @author ubuntu 2017/03/08.
 */
public class MapPath {
    private List<String> keyPath;

    private MapPath(List<String> keyPath) {
        this.keyPath = keyPath;
    }

    public static MapPath root() {
        return new MapPath(new ArrayList<>());
    }

    public MapPath join(String path) {
        ArrayList<String> copy = new ArrayList<>(keyPath);
        copy.add(path);

        return new MapPath(copy);
    }

    public Object find(Map<String, Object> map) throws UndefinedValueException {
        Map copy = new HashMap<>(map);
        int size = keyPath.size();

        if (size == 0) {
            return map;
        }

        for (int i = 0; i < size; i++) {
            String key = keyPath.get(i);
            Object o = copy.get(key);
            if (i == size - 1) {
                return o;
            }

            if (!(o instanceof Map)) throw newUndefinedValueException();

            copy = (Map)o;
        }

        throw newUndefinedValueException();
    }

    private UndefinedValueException newUndefinedValueException() {
        String key = keyPath.stream().collect(Collectors.joining("."));
        return new UndefinedValueException(String.format("Not found key[%s]", key));
    }

    @Override
    public String toString() {
        if (keyPath.isEmpty()) {
            return "[ROOT]";
        }
        return keyPath.stream().collect(Collectors.joining(".", "[", "]"));
    }
}
