package org.carbon.web.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.carbon.web.exception.ActionInvokeException;
import org.carbon.web.exception.WrappedException;

/**
 * @author Shota Oda 2017/01/07.
 */
public class ExecutableAction<RETURN> {
    private Object instance;
    private Method action;
    private ArgumentMetas arguments;

    public ExecutableAction(Object instance, Method action, ArgumentMetas arguments) {
        this.instance = instance;
        this.action = action;
        this.arguments = arguments;
    }

    public Collection<ArgumentMeta> getArgumentInfo() {
        return arguments.values();
    }

    @SuppressWarnings("unchecked")
    public RETURN execute() {
        Object[] args = collectArg();
        try {
            return (RETURN) action.invoke(instance, args);
        } catch (IllegalAccessException e) {
            throw new ActionInvokeException(e);
        } catch (InvocationTargetException e) {
            throw WrappedException.wrap(e.getTargetException());
        }
    }

    @SuppressWarnings("unchecked")
    public RETURN executeWith(ArgumentMeta... arguments) throws Exception {
        Object[] args = collectArg(arguments);
        return (RETURN) action.invoke(instance, args);
    }

    private Object[] collectArg(ArgumentMeta... additions) {
        ArgumentMetas source = new ArgumentMetas(arguments);
        for (ArgumentMeta addition : additions) {
            source.putMeta(addition);
        }
        return Arrays.stream(action.getParameters()).map(source::getValue).toArray();
    }
}
