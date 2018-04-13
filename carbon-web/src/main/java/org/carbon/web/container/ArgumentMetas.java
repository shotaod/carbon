package org.carbon.web.container;

import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shota.Oda 2018/02/22.
 */
public class ArgumentMetas {
    private Map<String, ArgumentMeta> metas;

    public ArgumentMetas() {
        this.metas = new HashMap<>();
    }

    public ArgumentMetas(ArgumentMetas argumentMetas) {
        this.metas = new HashMap<>(argumentMetas.metas);
    }

    public void putMeta(ArgumentMeta meta) {
        metas.put(meta.getParameter().getName(), meta);
    }

    public Object getValue(Parameter parameter) {
        return metas.get(parameter.getName()).getValue();
    }

    public Collection<ArgumentMeta> values() {
        return metas.values();
    }
}
