package org.dabuntu.web.core.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dabuntu.web.exception.RequestMappingException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * @author ubuntu 2016/10/12.
 */
public class JsonKeyValueMapper implements RequestMapper {

	@Override
	public Map<String, Object> map(HttpServletRequest request) {
		String requestBody = parseRequest(request);

		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.readValue(requestBody, new TypeReference<Map>(){});
		} catch (IOException e) {
			throw new RequestMappingException("", e);
		}
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
			throw new RequestMappingException("", e);
		}
	}
}
