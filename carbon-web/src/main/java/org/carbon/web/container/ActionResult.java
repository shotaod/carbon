package org.carbon.web.container;

/**
 * @author Shota Oda 2016/10/05.
 */
public class ActionResult {
    private Object result;
    private boolean noop;
    private static final ActionResult EMPTY = new ActionResult(null, true);

    private ActionResult(Object result, boolean noop) {
        this.result = result;
        this.noop = noop;
    }

    public static ActionResult Result(Object result) {
        return new ActionResult(result, false);
    }

    public static ActionResult Empty() {
        return EMPTY;
    }

    public Object getResult() {
        return result;
    }

    public boolean handled() {
        return noop;
    }
}
