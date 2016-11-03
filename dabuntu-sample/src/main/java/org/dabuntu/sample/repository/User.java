package org.dabuntu.sample.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ubuntu 2016/11/03.
 */
@Data
@AllArgsConstructor
public class User {
	private String username;
	private String email;
	private String encPassword;
}
