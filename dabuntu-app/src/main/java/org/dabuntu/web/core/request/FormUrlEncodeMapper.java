package org.dabuntu.web.core.request;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ubuntu 2016/11/29.
 */
public class FormUrlEncodeMapper implements RequestMapper {
	@Override
	public Map<String, Object> map(HttpServletRequest request) {

		HashMap<String, Object> keyValues = new HashMap<>();

		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String pName = params.nextElement();
			String pValue = request.getParameter(pName);
			keyValues.put(pName, pValue);
		}
		return keyValues;
	}
}
