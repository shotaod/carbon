package org.dabuntu.web.core.response.processor;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.core.response.AbstractResponseProcessor;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/10/14.
 */
@Component
public class JsonProcessor extends AbstractResponseProcessor {

	@Override
	public boolean process(HttpServletResponse response) {

		return false;
	}
}
