package org.carbon.web.container;

/**
 * @author Shota Oda 2016/10/05.
 */
public class ActionResult {
	private Object result;
    private Exception exception;
    private boolean noop;

    public ActionResult(Object result, Exception exception, boolean noop) {
        this.result = result;
        this.exception = exception;
        this.noop = noop;
    }

    public static ActionResult Result(Object result) {
        return new ActionResult(result, null, false);
    }

    public static ActionResult NoOp() {
        return new ActionResult(null, null, true);
    }

	public static ActionResult OnException(Exception e) {
        return new ActionResult(null, e, false);
    }

	public Object getResult() {
		return result;
	}
	public boolean hasException() {
     return exception != null;
    }
    public Exception getException() {
        return exception;
    }
    public boolean handled() {
        return noop;
    }
}
