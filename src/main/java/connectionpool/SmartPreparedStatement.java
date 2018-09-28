package connectionpool;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class SmartPreparedStatement extends SmartStatement implements PreparedStatement, Close {
    private PreparedStatement pstmt;
    private SmartConnection sConn;
    private boolean isClosed;
    Debugger debug = new Debugger("connectionpool.SmartPreparedStatement", true);

    SmartPreparedStatement(PreparedStatement pstmt, SmartConnection sConn) {
        super(pstmt, sConn);
        this.pstmt = pstmt;
        this.sConn = sConn;
    }

    private void preProcess() throws SQLException {
        if (isClosed()) {
            throw new SQLException("Prepared Statement already closed");
        }
        this.sConn.setLastAccessedTime();
    }

    public ResultSet executeQuery() throws SQLException {
        preProcess();

        return this.pstmt.executeQuery();
    }

    public int executeUpdate() throws SQLException {
        preProcess();

        return this.pstmt.executeUpdate();
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        preProcess();
        this.pstmt.setNull(parameterIndex, sqlType);
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        preProcess();
        this.pstmt.setBoolean(parameterIndex, x);
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        preProcess();
        this.pstmt.setByte(parameterIndex, x);
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        preProcess();
        this.pstmt.setShort(parameterIndex, x);
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        preProcess();
        this.pstmt.setInt(parameterIndex, x);
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        preProcess();
        this.pstmt.setLong(parameterIndex, x);
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        preProcess();
        this.pstmt.setFloat(parameterIndex, x);
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        preProcess();
        this.pstmt.setDouble(parameterIndex, x);
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        preProcess();
        this.pstmt.setBigDecimal(parameterIndex, x);
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        preProcess();
        this.pstmt.setString(parameterIndex, x);
    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        preProcess();
        this.pstmt.setBytes(parameterIndex, x);
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        preProcess();
        this.pstmt.setDate(parameterIndex, x);
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        preProcess();
        this.pstmt.setTime(parameterIndex, x);
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        preProcess();
        this.pstmt.setTimestamp(parameterIndex, x);
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        preProcess();
        this.pstmt.setAsciiStream(parameterIndex, x, length);
    }

    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        preProcess();
        this.pstmt.setUnicodeStream(parameterIndex, x, length);
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        preProcess();
        this.pstmt.setBinaryStream(parameterIndex, x, length);
    }

    public void clearParameters() throws SQLException {
        preProcess();
        this.pstmt.clearParameters();
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
        preProcess();
        this.pstmt.setObject(parameterIndex, x, targetSqlType, scale);
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        preProcess();
        this.pstmt.setObject(parameterIndex, x, targetSqlType);
    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        preProcess();
        this.pstmt.setObject(parameterIndex, x);
    }

    public boolean execute() throws SQLException {
        preProcess();

        return this.pstmt.execute();
    }

    public void addBatch() throws SQLException {
        preProcess();
        this.pstmt.addBatch();
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        preProcess();
        this.pstmt.setCharacterStream(parameterIndex, reader, length);
    }

    public void setRef(int i, Ref x) throws SQLException {
        preProcess();
        this.pstmt.setRef(i, x);
    }

    public void setBlob(int i, Blob x) throws SQLException {
        preProcess();
        this.pstmt.setBlob(i, x);
    }

    public void setClob(int i, Clob x) throws SQLException {
        preProcess();
        this.pstmt.setClob(i, x);
    }

    public void setArray(int i, Array x) throws SQLException {
        preProcess();
        this.pstmt.setArray(i, x);
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        preProcess();

        return this.pstmt.getMetaData();
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        preProcess();
        this.pstmt.setDate(parameterIndex, x, cal);
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        preProcess();
        this.pstmt.setTime(parameterIndex, x, cal);
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        preProcess();
        this.pstmt.setTimestamp(parameterIndex, x, cal);
    }

    public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
        preProcess();
        this.pstmt.setNull(paramIndex, sqlType, typeName);
    }

    public Connection getConnection() throws SQLException {
        preProcess();

        return this.pstmt.getConnection();
    }

    public int[] executeBatch() throws SQLException {
        preProcess();

        return this.pstmt.executeBatch();
    }

    public void clearBatch() throws SQLException {
        preProcess();
        this.pstmt.clearBatch();
    }

    public void addBatch(String sql) throws SQLException {
        preProcess();
        this.pstmt.addBatch(sql);
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        preProcess();

        return this.pstmt.executeQuery(sql);
    }

    public int executeUpdate(String sql) throws SQLException {
        preProcess();

        return this.pstmt.executeUpdate(sql);
    }

    public void close() throws SQLException {
        this.debug.print(String.valueOf("PreparedStatement Closed for:").concat(String.valueOf(this.sConn.getOwner())));
        if (this.isClosed) {
            throw new SQLException("PreparedStatement already closed");
        }
        this.pstmt.close();
        this.isClosed = true;
    }

    public boolean isClosed() throws SQLException {
        return this.isClosed;
    }

    public int getMaxFieldSize() throws SQLException {
        preProcess();

        return this.pstmt.getMaxFieldSize();
    }

    public void setMaxFieldSize(int max) throws SQLException {
        preProcess();
        this.pstmt.setMaxFieldSize(max);
    }

    public int getMaxRows() throws SQLException {
        preProcess();

        return this.pstmt.getMaxRows();
    }

    public void setMaxRows(int max) throws SQLException {
        preProcess();
        this.pstmt.setMaxRows(max);
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        preProcess();
        this.pstmt.setEscapeProcessing(enable);
    }

    public int getQueryTimeout() throws SQLException {
        preProcess();

        return this.pstmt.getQueryTimeout();
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        preProcess();
        this.pstmt.setQueryTimeout(seconds);
    }

    public void cancel() throws SQLException {
        preProcess();
        this.pstmt.cancel();
    }

    public SQLWarning getWarnings() throws SQLException {
        preProcess();

        return this.pstmt.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        preProcess();
        this.pstmt.clearWarnings();
    }

    public void setCursorName(String name) throws SQLException {
        preProcess();
        this.pstmt.setCursorName(name);
    }

    public boolean execute(String sql) throws SQLException {
        preProcess();

        return this.pstmt.execute(sql);
    }

    public ResultSet getResultSet() throws SQLException {
        preProcess();

        return this.pstmt.getResultSet();
    }

    public int getUpdateCount() throws SQLException {
        preProcess();

        return this.pstmt.getUpdateCount();
    }

    public boolean getMoreResults() throws SQLException {
        preProcess();

        return this.pstmt.getMoreResults();
    }

    public void setFetchDirection(int direction) throws SQLException {
        preProcess();
        this.pstmt.setFetchDirection(direction);
    }

    public int getFetchDirection() throws SQLException {
        preProcess();

        return this.pstmt.getFetchDirection();
    }

    public void setFetchSize(int rows) throws SQLException {
        preProcess();
        this.pstmt.setFetchSize(rows);
    }

    public int getFetchSize() throws SQLException {
        preProcess();

        return this.pstmt.getFetchSize();
    }

    public int getResultSetConcurrency() throws SQLException {
        preProcess();

        return this.pstmt.getResultSetConcurrency();
    }

    public int getResultSetType() throws SQLException {
        preProcess();

        return this.pstmt.getResultSetType();
    }

    public String toString() {
        return this.pstmt.toString();
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNString(int parameterIndex, String value) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        // TODO Auto-generated method stub

    }
}
