package org.carbon.persistent.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Shota.Oda 2018/02/25.
 */
public interface ConnectionManager {

    Connection allocateConnection() throws SQLException;

    void releaseConnection(Connection connection) throws SQLException;
}
