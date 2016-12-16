package org.carbon.web.core.response;

/**
 * @author ubuntu 2016/11/28.
 */
public class RedirectOperation implements HttpOperation {
	private String redirectTo;

	public static RedirectOperation to(String to) {
        return new RedirectOperation(to);
    }

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
