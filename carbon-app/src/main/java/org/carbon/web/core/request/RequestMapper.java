package org.carbon.web.core.request;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.exception.RequestMappingException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author ubuntu 2016/10/12.
 */
@Component
public class RequestMapper {
	@Inject
	private RequestMapperFactory factory;

	public <T> T map(HttpServletRequest request, Class<T> mapTo) {
		String contentType = Optional.ofNullable(request.getHeader("content-type"))
			.map(ct -> ct.toLowerCase())
			.orElse(null);
		return factory.factorize(contentType)
			.map(mapper -> mapper.map(request, mapTo))
			.orElseThrow(() -> new RequestMappingException("content-type not supported: " + contentType));
	}
}
