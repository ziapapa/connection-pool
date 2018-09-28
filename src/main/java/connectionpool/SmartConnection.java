package connectionpool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class SmartConnection implements Connection, Close, ConnectionMonitor {
    private Connection conn = null;
    private long lastAccessedTime = 0L;
    private long connectionObtainedTime = 0L;
    private ArrayList references;
    private boolean autoClose = false;
    private Debugger debug;
    private boolean isCurrentlyInTransaction;
    private Pool poolReference;
    private boolean isClosed = false;
    private boolean isForcedClosed = false;
    private long forcedCloseTime = 0L;
    private String owner;

    SmartConnection(Connection conn, Pool poolReference, String owner, boolean autoClose) {
        if (conn == null) {
            throw new IllegalArgumentException("Connection Object is null/closed");
        }
        this.conn = conn;
        this.poolReference = poolReference;
        this.owner = owner;
        this.lastAccessedTime = System.currentTimeMillis();
        this.connectionObtainedTime = this.lastAccessedTime;
        this.autoClose = autoClose;
        if (autoClose) {
            this.references = new ArrayList();
        }
        this.debug = new Debugger("SmartConnection", true);
    }

    SmartConnection(Connection conn, Pool poolReference) {
        this(conn, poolReference, "N/A", false);
    }

    private void preProcess() throws SQLException {
        if (this.isForcedClosed) {
            throw new StaleConnectionException(String
                    .valueOf(
                            "SmartPool had withdrawn the connection since it was idle for more than the specified time. Connection withdrawn at ")
                    .concat(String.valueOf(new Date(this.forcedCloseTime))));
        }
        if (this.isClosed) {
            throw new SQLException("Connection already closed");
        }
        this.lastAccessedTime = System.currentTimeMillis();
    }

    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    void setLastAccessedTime() {
        this.lastAccessedTime = System.currentTimeMillis();
    }

    public boolean isCurrentlyInTransaction() {
        return this.isCurrentlyInTransaction;
    }

    public long getConnectionObtainedTime() {
        return this.connectionObtainedTime;
    }

    public String getOwner() {
        return this.owner;
    }

    public Statement createStatement() throws SQLException {
        preProcess();
        SmartStatement stmt = new SmartStatement(this.conn.createStatement(), this);
        if (this.autoClose) {
            this.references.add(stmt);
        }
        return stmt;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        preProcess();
        SmartPreparedStatement pstmt = new SmartPreparedStatement(this.conn.prepareStatement(sql), this);
        if (this.autoClose) {
            this.references.add(pstmt);
        }
        return pstmt;
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        preProcess();
        SmartCallableStatement cstmt = new SmartCallableStatement(this.conn.prepareCall(sql), this);
        if (this.autoClose) {
            this.references.add(cstmt);
        }
        return cstmt;
    }

    public String nativeSQL(String sql) throws SQLException {
        preProcess();

        return this.conn.nativeSQL(sql);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        preProcess();
        this.isCurrentlyInTransaction = autoCommit;
        this.conn.setAutoCommit(autoCommit);
    }

    public boolean getAutoCommit() throws SQLException {
        preProcess();

        return this.conn.getAutoCommit();
    }

    public void commit() throws SQLException {
        preProcess();
        this.conn.commit();
    }

    public void rollback() throws SQLException {
        preProcess();
        this.conn.rollback();
    }

    void forcedClose() {
        try {
            SmartConnection localSmartConnection = this;
            try {
                this.forcedCloseTime = System.currentTimeMillis();
                this.debug.print(
                        String.valueOf("Forcefully closing connection for owner").concat(String.valueOf(this.owner)));
                close();
                this.isForcedClosed = true;
            } finally {
            }
        } catch (SQLException sqe) {
            this.debug.writeException(sqe);
        }
    }

    public void close() throws SQLException {
        preProcess();
        if (this.autoClose) {
            closeAll();
        }
        if ((!this.conn.getAutoCommit())
                && (((ConnectionPool) this.poolReference).getConfigMonitor().getConnectionLoaderClass() != null)) {
            this.conn.rollback();
            this.conn.setAutoCommit(true);
        }
        this.debug.print(String.valueOf("Connection Released for:").concat(String.valueOf(getOwner())));
        this.poolReference.returnConnection(this);
        this.isClosed = true;
    }

    private void closeAll() throws SQLException {
        for (int i = 0; i < this.references.size(); i++) {
            Close close = (Close) this.references.get(i);
            this.debug.print(String.valueOf("Closing ").concat(String.valueOf(close)));
            if (!close.isClosed()) {
                close.close();
            }
        }
    }

    public boolean isClosed() throws SQLException {
        return this.isClosed;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        preProcess();

        return this.conn.getMetaData();
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        preProcess();
        this.conn.setReadOnly(readOnly);
    }

    public boolean isReadOnly() throws SQLException {
        preProcess();

        return this.conn.isReadOnly();
    }

    public void setCatalog(String catalog) throws SQLException {
        preProcess();
        this.conn.setCatalog(catalog);
    }

    public String getCatalog() throws SQLException {
        preProcess();

        return this.conn.getCatalog();
    }

    public void setTransactionIsolation(int level) throws SQLException {
        preProcess();
        this.conn.setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException {
        preProcess();

        return this.conn.getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException {
        preProcess();

        return this.conn.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        preProcess();
        this.conn.clearWarnings();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        preProcess();
        SmartStatement stmt = new SmartStatement(this.conn.createStatement(resultSetType, resultSetConcurrency), this);
        if (this.autoClose) {
            this.references.add(stmt);
        }
        return stmt;
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        preProcess();
        SmartPreparedStatement pstmt = new SmartPreparedStatement(
                this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency), this);
        if (this.autoClose) {
            this.references.add(pstmt);
        }
        return pstmt;
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        preProcess();
        SmartCallableStatement cstmt = new SmartCallableStatement(
                this.conn.prepareCall(sql, resultSetType, resultSetConcurrency), this);
        if (this.autoClose) {
            this.references.add(cstmt);
        }
        return cstmt;
    }

    public Map getTypeMap() throws SQLException {
        preProcess();

        return this.conn.getTypeMap();
    }

    public void setTypeMap(Map map) throws SQLException {
        preProcess();
        setTypeMap(map);
    }

    Connection returnConnection() {
        return this.conn;
    }

    public String toString() {
        return this.conn.toString();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public void setHoldability(int holdability) throws SQLException {
        // TODO Auto-generated method stub

    }

    public int getHoldability() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public Savepoint setSavepoint() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        // TODO Auto-generated method stub

    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Clob createClob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Blob createBlob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public NClob createNClob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public SQLXML createSQLXML() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isValid(int timeout) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        // TODO Auto-generated method stub

    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        // TODO Auto-generated method stub

    }

    public String getClientInfo(String name) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Properties getClientInfo() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setSchema(String schema) throws SQLException {
        // TODO Auto-generated method stub

    }

    public String getSchema() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public void abort(Executor executor) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        // TODO Auto-generated method stub

    }

    public int getNetworkTimeout() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }
}
