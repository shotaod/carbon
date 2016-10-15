package org.dabuntu.web.core.response;

/**
 * @author ubuntu 2016/10/14.
 */
public abstract class AbstractResponseProcessor implements ResponseProcessor {
	protected Object result;

	public AbstractResponseProcessor with(Object result) {
		this.result = result;
		return this;
	}
}
