package org.dabuntu.web.core.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.exception.RequestMappingException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * @author ubuntu 2016/10/12.
 */
@Component
public class JsonKeyValueRequestMapper implements TypeSafeRequestMapper {

	@Override
	public <T> T map(HttpServletRequest request, Class<T> mapTo) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.readValue(request.getReader(), new TypeReference<T>(){});
		} catch (IOException e) {
			throw new RequestMappingException("json mapping exception", e);
		}
	}
}
