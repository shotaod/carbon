package org.dabuntu.web.core.response;

/**
 * @author ubuntu 2016/11/28.
 */
public interface HttpOperation {
	enum Strategy {
		Forward("forward"),
		Redirect("redirect"),
        Noop("noop")
		;
		private String code;

		Strategy(String code) {
			this.code = code;
		}
	}

	Strategy getStrategy();
	String getPathTo();
}
