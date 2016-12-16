package org.carbon.persistent;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author ubuntu 2016/11/13.
 */
@Data
public class DataSourceConfig {
	private String url;
    private String db;
	private String user;
	private String password;
	private Integer port;

	public DataSource toMysqlDataSource() {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl(String.format("jdbc:mysql://%s:%s/%s",url,port,db));
		dataSource.setPort(port);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		try {
			dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataSource;
	}
}
