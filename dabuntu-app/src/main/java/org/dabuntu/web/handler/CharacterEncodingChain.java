package org.dabuntu.web.handler;

import org.dabuntu.component.annotation.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/10/22.
 */
@Component
public class CharacterEncodingChain extends HttpHandlerChain{

	private static final String encode = "utf-8";

	@Override
	protected void chain(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding(encode);
		super.chain(request, response);
	}
}
