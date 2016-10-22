package org.dabuntu.web.core;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.container.request.MappedCookie;
import org.dabuntu.web.container.request.MappedRequestBody;
import org.dabuntu.web.container.request.ParsedRequest;
import org.dabuntu.web.core.request.JsonKeyValueMapper;
import org.dabuntu.web.core.request.KeyValueMapper;
import org.dabuntu.web.core.request.MultipartFormKeyValueMapper;
import org.dabuntu.web.def.HttpMethod;
import org.dabuntu.web.exception.RequestMappingException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/12.
 */
@Component
public class RequestParser {

	// -----------------------------------------------------
	//                              applicable content types
	//                              ------------------------
	private static final String APPLICATION_JSON = "application/json";
	private static final String MULTIPART_FORM = "multipart/form-data";
	private static final String X_WWW_FORM_URL_ENCODE = "application/x-www-form-urlencoded";

	public ParsedRequest parse(HttpServletRequest request) {

		MappedCookie mappedCookie = this.extractCookie(request);

		// if Http Method is GET, process only cookie
		if (HttpMethod.GET.getCode().equals(request.getMethod())) {
			return ParsedRequest.forGet(mappedCookie);
		}

		Map<String, Object> map = this.contentMapperFactory(request).map();
		MappedRequestBody mappedReqBody = new MappedRequestBody(map);

		return new ParsedRequest(request.getMethod(), mappedCookie, mappedReqBody);
	}

	private MappedCookie extractCookie(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader("Cookie"))
			.map(rawCookie -> {
				Map<String, String> map = Arrays.stream(rawCookie.split(";"))
						.map(String::trim)
						.collect(Collectors.toMap(
								kv -> kv.split("=")[0],
								kv -> kv.split("=")[1]
						));
				return new MappedCookie(map);
			}).orElseGet(() -> MappedCookie.empty());
	}

	private KeyValueMapper contentMapperFactory(HttpServletRequest request) {
		String contentType = Optional.ofNullable(request.getHeader("content-type")).map(ct -> ct.toLowerCase()).orElse(null);

		if (APPLICATION_JSON.equals(contentType)) {
			return new JsonKeyValueMapper(request);
		}

		if (MULTIPART_FORM.equals(contentType)) {
			return new MultipartFormKeyValueMapper(request);
		}

		if (X_WWW_FORM_URL_ENCODE.equals(contentType) || contentType == null) {
			return () -> {
				HashMap<String, Object> keyValues = new HashMap<>();

				Enumeration<String> params = request.getParameterNames();
				while (params.hasMoreElements()) {
					String pName = params.nextElement();
					String pValue = request.getParameter(pName);
					keyValues.put(pName, pValue);
				}

				return keyValues;
			};
		}

		throw new RequestMappingException(String.format("Not Found suitable request mapper For content-type ['%s']", contentType));
	}
}
