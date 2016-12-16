package org.carbon.web.container;

/**
 * @author ubuntu 2016/10/05.
 */
public class ActionResult {
	private Object result;

	public ActionResult(Object result) {
		this.result = result;
	}

	public Object getResult() {
		return result;
	}
}
