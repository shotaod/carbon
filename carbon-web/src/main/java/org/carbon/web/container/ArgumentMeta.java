package org.carbon.web.container;

import java.lang.reflect.Parameter;

/**
 * @author Shota Oda 2016/10/08.
 */
public class ArgumentMeta {
    private Parameter parameter;
    // extracted value from somewhere
    private Object value;

    public ArgumentMeta(Parameter parameter, Object value) {
        this.parameter = parameter;
        this.value = value;
    }

    public Class getType() {
        return parameter.getType();
    }

    public Parameter getParameter() {
        return parameter;
    }

    public boolean isResolved() {
        return value != null;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
