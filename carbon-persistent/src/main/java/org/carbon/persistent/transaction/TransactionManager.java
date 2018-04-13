package org.carbon.persistent.transaction;

import java.sql.SQLException;

/**
 * @author garden 2018/04/07.
 */
public interface TransactionManager {
    void begin() throws SQLException;

    void end() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;
}
