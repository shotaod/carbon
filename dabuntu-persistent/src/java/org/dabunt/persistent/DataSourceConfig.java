package org.dabunt.persistent;

import lombok.Data;

/**
 * @author ubuntu 2016/11/13.
 */
@Data
public class DataSourceConfig {
	private String url;
	private String user;
	private String password;
	private Integer port;
}
