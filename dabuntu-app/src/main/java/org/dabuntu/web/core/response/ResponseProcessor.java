package org.dabuntu.web.core.response;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

/**
 * @author ubuntu 2016/10/14.
 */
public interface ResponseProcessor {
	boolean process (HttpServletResponse response);
}
