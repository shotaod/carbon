package org.dabuntu.web.core.response;

/**
 * @author ubuntu 2016/11/28.
 */
public class RedirectOperation implements HttpOperation {
	private String redirectTo;

	public RedirectOperation(String redirectTo) {
		this.redirectTo = redirectTo;
	}

	@Override
	public Strategy getStrategy() {
		return Strategy.Redirect;
	}

	@Override
	public String getPathTo() {
		return redirectTo;
	}

}
