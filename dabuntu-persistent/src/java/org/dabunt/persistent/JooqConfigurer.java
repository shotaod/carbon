package org.dabunt.persistent;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;

import javax.sql.DataSource;

/**
 * @author ubuntu 2016/11/26.
 */
public class JooqConfigurer {
	private DataSource dataSource;

	public JooqConfigurer(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DSLContext createDSLContext() {
		return new DefaultDSLContext(dataSource, SQLDialect.MYSQL);
	}
}
