package org.dabuntu.web.core;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.web.container.request.MappedCookie;
import org.dabuntu.web.container.request.MappedRequestBody;
import org.dabuntu.web.container.request.ParsedRequest;
import org.dabuntu.web.core.request.RequestMapper;
import org.dabuntu.web.core.request.RequestMapperFactory;
import org.dabuntu.web.def.HttpMethod;
import org.dabuntu.web.exception.RequestMappingException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/12.
 */
@Component
public class RequestParser {

	@Inject
	private RequestMapperFactory factory;

	public ParsedRequest parse(HttpServletRequest request) {

		MappedCookie mappedCookie = this.extractCookie(request);

		// if Http Method is GET, process only cookie
		if (HttpMethod.GET.getCode().equals(request.getMethod())) {
			return ParsedRequest.forGet(mappedCookie);
		}
		RequestMapper mapper = factory.factorize(request).orElseThrow(() ->
			new RequestMappingException(String
				.format("Not Found suitable request mapper For content-type ['%s']",
					request.getContentType())));

		Map<String, Object> map = mapper.map(request);
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
}
