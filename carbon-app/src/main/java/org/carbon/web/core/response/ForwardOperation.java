package org.carbon.web.core.response;

/**
 * @author ubuntu 2016/11/28.
 */
public class ForwardOperation implements HttpOperation {
	private String forwardTo;

	public ForwardOperation(String forwardTo) {
		this.forwardTo = forwardTo;
	}

	@Override
	public Strategy getStrategy() {
		return Strategy.Forward;
	}

	@Override
	public String getPathTo() {
		return forwardTo;
	}
}
