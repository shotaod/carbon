package org.carbon.persistent.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author garden 2018/04/07.
 */
@Component
public class DefaultTransactionalConnectionManager implements TransactionalConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(DefaultTransactionalConnectionManager.class);
    private static final ThreadLocal<DefaultTransactionalConnectionManager> threadBindContext =
            ThreadLocal.withInitial(DefaultTransactionalConnectionManager::new);

    @Assemble
    private static DataSource dataSource;

    private enum TransactionState {
        NONE,
        BEGAN,
    }

    private Connection transactionConnection;
    private TransactionState transactionState;
    private int transactionDepth;

    public DefaultTransactionalConnectionManager() {
        transactionDepth = 0;
        transactionState = TransactionState.NONE;
    }

    private DefaultTransactionalConnectionManager currentContext() {
        return threadBindContext.get();
    }

    private void removeContext() {
        threadBindContext.remove();
    }

    @Override
    public Connection allocateConnection() throws SQLException {
        logDebugAction("allocate");
        DefaultTransactionalConnectionManager self = currentContext();

        if (self.transactionState == TransactionState.NONE)
            return dataSource.getConnection();

        // if BEGIN
        if (self.transactionConnection == null) {
            self.transactionConnection = dataSource.getConnection();
            self.transactionConnection.setAutoCommit(false);
        }
        return self.transactionConnection;
    }

    @Override
    public void releaseConnection(Connection connection) throws SQLException {
        logDebugAction("release");
        DefaultTransactionalConnectionManager self = currentContext();
        if (self.transactionState == TransactionState.BEGAN) return;

        try {
            connection.close();
        } finally {
            removeContext();
        }
    }

    @Override
    public void begin() {
        logDebugAction("begin");
        DefaultTransactionalConnectionManager self = currentContext();
        self.transactionState = TransactionState.BEGAN;
        self.transactionDepth++;
    }

    @Override
    public void end() throws SQLException {
        logDebugAction("end");
        DefaultTransactionalConnectionManager self = currentContext();
        if (self.transactionConnection != null) {
            self.transactionConnection.close();
        }
        removeContext();
    }

    @Override
    public void commit() throws SQLException {
        logDebugAction("commit");
        DefaultTransactionalConnectionManager self = currentContext();
        self.transactionDepth--;
        if (self.transactionDepth == 0) {
            if (currentContext().transactionConnection == null) {
                throw new IllegalStateException("transaction is began but connection is null");
            }
            self.transactionConnection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        logDebugAction("rollback");
        if (currentContext().transactionConnection != null) {
            currentContext().transactionConnection.rollback();
        }
    }

    private void logDebugAction(String action) {
        DefaultTransactionalConnectionManager self = currentContext();
        TransactionState state = self.transactionState;
        int depth = self.transactionDepth;
        logger.debug("[persistent{TRAN: {}, DEPTH: {}}] {}", state, depth, action);
    }
}
