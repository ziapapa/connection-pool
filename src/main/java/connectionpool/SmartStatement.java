package connectionpool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

public class SmartStatement implements Statement, Close {
    private Statement stmt;
    private boolean isClosed = false;
    private SmartConnection sConn;
    private Debugger debug = new Debugger("SmartStatement", true);

    SmartStatement(Statement stmt, SmartConnection sConn) {
        this.stmt = stmt;
        this.sConn = sConn;
    }

    private void preProcess() throws SQLException {
        if (isClosed()) {
            throw new SQLException("Statement already closed");
        }
        this.sConn.setLastAccessedTime();
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        preProcess();

        return this.stmt.executeQuery(sql);
    }

    public int executeUpdate(String sql) throws SQLException {
        preProcess();

        return this.stmt.executeUpdate(sql);
    }

    public void close() throws SQLException {
        if (this.isClosed) {
            throw new SQLException("Statement already closed");
        }
        this.stmt.close();
        this.debug.print(String.valueOf("Statement Closed for:").concat(String.valueOf(this.sConn.getOwner())));
        this.isClosed = true;
    }

    public boolean isClosed() throws SQLException {
        return this.isClosed;
    }

    public int getMaxFieldSize() throws SQLException {
        preProcess();

        return this.stmt.getMaxFieldSize();
    }

    public void setMaxFieldSize(int max) throws SQLException {
        preProcess();
        this.stmt.setMaxFieldSize(max);
    }

    public int getMaxRows() throws SQLException {
        preProcess();

        return this.stmt.getMaxRows();
    }

    public void setMaxRows(int max) throws SQLException {
        preProcess();
        this.stmt.setMaxRows(max);
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        preProcess();
        this.stmt.setEscapeProcessing(enable);
    }

    public int getQueryTimeout() throws SQLException {
        preProcess();

        return this.stmt.getQueryTimeout();
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        preProcess();
        this.stmt.setQueryTimeout(seconds);
    }

    public void cancel() throws SQLException {
        preProcess();
        this.stmt.cancel();
    }

    public SQLWarning getWarnings() throws SQLException {
        preProcess();

        return this.stmt.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        preProcess();
        this.stmt.clearWarnings();
    }

    public void setCursorName(String name) throws SQLException {
        preProcess();
        this.stmt.setCursorName(name);
    }

    public boolean execute(String sql) throws SQLException {
        preProcess();

        return this.stmt.execute(sql);
    }

    public ResultSet getResultSet() throws SQLException {
        preProcess();

        return this.stmt.getResultSet();
    }

    public int getUpdateCount() throws SQLException {
        preProcess();

        return this.stmt.getUpdateCount();
    }

    public boolean getMoreResults() throws SQLException {
        preProcess();

        return this.stmt.getMoreResults();
    }

    public void setFetchDirection(int direction) throws SQLException {
        preProcess();
        this.stmt.setFetchDirection(direction);
    }

    public int getFetchDirection() throws SQLException {
        preProcess();

        return this.stmt.getFetchDirection();
    }

    public void setFetchSize(int rows) throws SQLException {
        preProcess();
        this.stmt.setFetchSize(rows);
    }

    public int getFetchSize() throws SQLException {
        preProcess();

        return this.stmt.getFetchSize();
    }

    public int getResultSetConcurrency() throws SQLException {
        preProcess();

        return this.stmt.getResultSetConcurrency();
    }

    public int getResultSetType() throws SQLException {
        preProcess();

        return this.stmt.getResultSetType();
    }

    public void addBatch(String sql) throws SQLException {
        preProcess();
        this.stmt.addBatch(sql);
    }

    public void clearBatch() throws SQLException {
        preProcess();
        this.stmt.clearBatch();
    }

    public int[] executeBatch() throws SQLException {
        preProcess();

        return this.stmt.executeBatch();
    }

    public Connection getConnection() throws SQLException {
        preProcess();

        return this.stmt.getConnection();
    }

    public String toString() {
        return this.stmt.toString();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean getMoreResults(int current) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean execute(String sql, String[] columnNames) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public int getResultSetHoldability() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setPoolable(boolean poolable) throws SQLException {
        // TODO Auto-generated method stub

    }

    public boolean isPoolable() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public void closeOnCompletion() throws SQLException {
        // TODO Auto-generated method stub

    }

    public boolean isCloseOnCompletion() throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }
}
