package org.dabuntu.web.core.request;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author ubuntu 2016/10/12.
 */
public interface RequestMapper {
	Map<String, Object> map(HttpServletRequest request);
}
