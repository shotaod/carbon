package org.dabuntu.web.core.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dabuntu.web.exception.RequestMappingException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ubuntu 2016/10/12.
 */
public class JsonKeyValueMapper extends AbstractKeyValueMapper {

	public JsonKeyValueMapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Map<String, Object> doMap(HttpServletRequest request) {
		String requestBody = parseRequest(request);
		HashMap<String, Object> result = new HashMap<>();

		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.readValue(requestBody, new TypeReference<Map>(){});
		} catch (IOException e) {
			throw new RequestMappingException();
		}
	}
}
