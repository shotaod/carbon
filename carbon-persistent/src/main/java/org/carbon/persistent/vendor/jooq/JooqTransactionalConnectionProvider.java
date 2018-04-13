package org.carbon.persistent.vendor.jooq;

import java.sql.Connection;
import java.sql.SQLException;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.persistent.transaction.TransactionalConnectionManager;
import org.jooq.ConnectionProvider;
import org.jooq.exception.DataAccessException;

/**
 * @author Shota.Oda 2018/02/25.
 */
@Component
public class JooqTransactionalConnectionProvider implements ConnectionProvider {

    @Assemble
    private TransactionalConnectionManager connectionManager;

    @Override
    public Connection acquire() throws DataAccessException {
        try {
            return connectionManager.allocateConnection();
        } catch (SQLException e) {
            throw new DataAccessException("cannot acquire connection", e);
        }
    }

    @Override
    public void release(Connection connection) throws DataAccessException {
        try {
            connectionManager.releaseConnection(connection);
        } catch (SQLException e) {
            throw new DataAccessException("cannot release connection", e);
        }
    }
}
