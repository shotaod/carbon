package org.carbon.web.auth;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Shota Oda 2016/11/03.
 */
public interface AuthRequestMapper {
	class AuthInfo {
		private String username;
		private String password;

		public AuthInfo(String username, String password) {
			this.username = username;
			this.password = password;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
	}

	Optional<AuthInfo> map(HttpServletRequest request);
}
