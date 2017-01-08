package org.carbon.web.container;

import org.carbon.web.exception.ActionInvokeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author ubuntu 2017/01/07.
 */
public class ExecutableAction<RETURN> {
    private Object instance;
    private Method action;
    private Map<String, ArgumentMeta> arguments;

    public ExecutableAction(Class<RETURN> type, Object instance, Method action, Map<String, ArgumentMeta> arguments) {
        this.instance = instance;
        this.action = action;
        this.arguments = arguments;
    }

    public Map<String, ArgumentMeta> getArgumentInfo() {
        return arguments;
    }

    @SuppressWarnings("unchecked")
    public RETURN execute() {
        Object[] args = collectArg(arguments);
        try {
            return (RETURN) action.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ActionInvokeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public RETURN executeWith(Map<String, ArgumentMeta> arguments) throws Exception {
        Object[] args = collectArg(arguments);
        return (RETURN) action.invoke(instance, args);
    }

    private Object[] collectArg(Map<String, ArgumentMeta> source) {
        return Arrays.stream(action.getParameters()).map(param -> source.get(param.getName()).getValue()).toArray();
    }
}
