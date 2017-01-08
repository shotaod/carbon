package org.carbon.persistent;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Shota Oda 2016/11/13.
 */
public class DataSourceConfig {
	private String url;
    private String db;
	private String user;
	private String password;
	private Integer port;

	public void setUrl(String url) {
		this.url = url;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public DataSource toMysqlDataSource() {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl(String.format("jdbc:mysql://%s:%s/%s",url,port,db));
		dataSource.setPort(port);
		dataSource.setUser(user);
		dataSource.setPassword(password);
//		try {
//			dataSource.getConnection();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return dataSource;
	}
}
