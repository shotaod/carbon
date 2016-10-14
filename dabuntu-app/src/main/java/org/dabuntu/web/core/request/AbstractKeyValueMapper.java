package org.dabuntu.web.core.request;

import org.dabuntu.web.exception.RequestMappingException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * @author ubuntu 2016/10/12.
 */
public abstract class AbstractKeyValueMapper implements KeyValueMapper {

	private HttpServletRequest request;
	abstract  Map<String, Object> doMap(HttpServletRequest request);

	public Map<String, Object> map() {
		return this.doMap(this.request);
	}

	public AbstractKeyValueMapper(HttpServletRequest request) {
		this.request = request;
	}

	protected String parseRequest(HttpServletRequest request) {
		try (BufferedReader reader = request.getReader()) {
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			throw new RequestMappingException();
		}
	}
}
