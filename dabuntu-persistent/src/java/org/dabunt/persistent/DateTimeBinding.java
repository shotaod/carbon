package org.dabunt.persistent;

import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Converter;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author ubuntu 2016/11/28.
 */
public class DateTimeBinding implements Binding<Timestamp, LocalDateTime> {
	@Override
	public Converter<Timestamp, LocalDateTime> converter() {
		return new Converter<Timestamp, LocalDateTime>() {
			@Override
			public LocalDateTime from(Timestamp databaseObject) {
				return null;
			}

			@Override
			public Timestamp to(LocalDateTime userObject) {
				return null;
			}

			@Override
			public Class<Timestamp> fromType() {
				return null;
			}

			@Override
			public Class<LocalDateTime> toType() {
				return null;
			}
		};
	}

	@Override
	public void sql(BindingSQLContext<LocalDateTime> ctx) throws SQLException {

	}

	@Override
	public void register(BindingRegisterContext<LocalDateTime> ctx) throws SQLException {

	}

	@Override
	public void set(BindingSetStatementContext<LocalDateTime> ctx) throws SQLException {

	}

	@Override
	public void set(BindingSetSQLOutputContext<LocalDateTime> ctx) throws SQLException {

	}

	@Override
	public void get(BindingGetResultSetContext<LocalDateTime> ctx) throws SQLException {

	}

	@Override
	public void get(BindingGetStatementContext<LocalDateTime> ctx) throws SQLException {

	}

	@Override
	public void get(BindingGetSQLInputContext<LocalDateTime> ctx) throws SQLException {

	}
}
