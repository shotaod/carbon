package org.dabuntu.web.core.request;

import org.dabuntu.component.annotation.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author ubuntu 2016/11/29.
 */
@Component
public class RequestMapperFactory {

	// -----------------------------------------------------
	//                              applicable content types
	//                              ------------------------
	private static final String APPLICATION_JSON = "application/json";
	private static final String MULTIPART_FORM = "multipart/form-data";
	private static final String X_WWW_FORM_URL_ENCODE = "application/x-www-form-urlencoded";

	public Optional<RequestMapper> factorize(HttpServletRequest request) {
		String contentType = Optional.ofNullable(request.getHeader("content-type")).map(ct -> ct.toLowerCase()).orElse(null);

		if (APPLICATION_JSON.equals(contentType)) {
			return Optional.of(new JsonKeyValueMapper());
		}

		if (MULTIPART_FORM.equals(contentType)) {
			return Optional.of(new MultipartFormKeyValueMapper());
		}

		if (X_WWW_FORM_URL_ENCODE.equals(contentType) || contentType == null) {
			return Optional.of(new FormUrlEncodeMapper());
		}

		return Optional.empty();
	}
}
