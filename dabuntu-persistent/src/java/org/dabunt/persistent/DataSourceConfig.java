package org.dabunt.persistent;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.Data;

import javax.sql.DataSource;

/**
 * @author ubuntu 2016/11/13.
 */
@Data
public class DataSourceConfig {
	private String url;
	private String user;
	private String password;
	private Integer port;

	public DataSource toMysqlDataSource() {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl(url);
		dataSource.setPort(port);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		return dataSource;
	}
}
