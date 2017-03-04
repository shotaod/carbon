package org.carbon.persistent;

import java.sql.SQLException;
import javax.sql.DataSource;

import org.carbon.persistent.exception.ConnectionTestException;

/**
 * @author ubuntu 2017/03/04.
 */
public class ConnectionTester {
    public static void testConnection(DataSource dataSource) {
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConnectionTestException(e);
        }
    }
}
