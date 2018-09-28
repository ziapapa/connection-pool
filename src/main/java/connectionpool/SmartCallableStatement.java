package connectionpool;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class SmartCallableStatement extends SmartPreparedStatement implements Close, CallableStatement {
    private CallableStatement cstmt;
    private SmartConnection sConn;
    private boolean isClosed = false;
    Debugger debug = new Debugger("connectionpool.SmartCallableStatement", true);

    SmartCallableStatement(CallableStatement cstmt, SmartConnection sConn) {
        super(cstmt, sConn);
        this.cstmt = cstmt;
        this.sConn = sConn;
    }

    private void preProcess() throws SQLException {
        if (isClosed()) {
            throw new SQLException("Prepared Statement already closed");
        }
        this.sConn.setLastAccessedTime();
    }

    public void registerOutParameter(int p0, int p1) throws SQLException {
        preProcess();
        this.cstmt.registerOutParameter(p0, p1);
    }

    public void registerOutParameter(int p0, int p1, int p2) throws SQLException {
        preProcess();
        this.cstmt.registerOutParameter(p0, p1, p2);
    }

    public boolean wasNull() throws SQLException {
        preProcess();

        return this.cstmt.wasNull();
    }

    public String getString(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getString(p0);
    }

    public boolean getBoolean(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getBoolean(p0);
    }

    public byte getByte(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getByte(p0);
    }

    public short getShort(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getShort(p0);
    }

    public int getInt(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getInt(p0);
    }

    public long getLong(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getLong(p0);
    }

    public float getFloat(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getFloat(p0);
    }

    public double getDouble(int p0) throws SQLException {
        preProcess();

        return getDouble(p0);
    }

    public BigDecimal getBigDecimal(int p0, int p1) throws SQLException {
        preProcess();

        return getBigDecimal(p0, p1);
    }

    public byte[] getBytes(int p0) throws SQLException {
        preProcess();

        return getBytes(p0);
    }

    public Date getDate(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getDate(p0);
    }

    public Time getTime(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getTime(p0);
    }

    public Timestamp getTimestamp(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getTimestamp(p0);
    }

    public Object getObject(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getObject(p0);
    }

    public BigDecimal getBigDecimal(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getBigDecimal(p0);
    }

    public Object getObject(int p0, Map p1) throws SQLException {
        preProcess();

        return this.cstmt.getObject(p0, p1);
    }

    public Ref getRef(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getRef(p0);
    }

    public Blob getBlob(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getBlob(p0);
    }

    public Clob getClob(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getClob(p0);
    }

    public Array getArray(int p0) throws SQLException {
        preProcess();

        return this.cstmt.getArray(p0);
    }

    public Date getDate(int p0, Calendar p1) throws SQLException {
        preProcess();

        return this.cstmt.getDate(p0, p1);
    }

    public Time getTime(int p0, Calendar p1) throws SQLException {
        preProcess();

        return this.cstmt.getTime(p0, p1);
    }

    public Timestamp getTimestamp(int p0, Calendar p1) throws SQLException {
        preProcess();

        return this.cstmt.getTimestamp(p0, p1);
    }

    public void registerOutParameter(int p0, int p1, String p2) throws SQLException {
        preProcess();
        this.cstmt.registerOutParameter(p0, p1, p2);
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public String toString() {
        return this.cstmt.toString();
    }

    public void close() throws SQLException {
        if (this.isClosed) {
            throw new SQLException("CallableStatement already closed");
        }
        this.cstmt.close();
        this.debug.print(String.valueOf("CallableStatement Closed for:").concat(String.valueOf(this.sConn.getOwner())));
        this.isClosed = true;
    }

    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        // TODO Auto-generated method stub

    }

    public URL getURL(int parameterIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setURL(String parameterName, URL val) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNull(String parameterName, int sqlType) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBoolean(String parameterName, boolean x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setByte(String parameterName, byte x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setShort(String parameterName, short x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setInt(String parameterName, int x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setLong(String parameterName, long x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setFloat(String parameterName, float x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setDouble(String parameterName, double x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setString(String parameterName, String x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBytes(String parameterName, byte[] x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setDate(String parameterName, Date x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setTime(String parameterName, Time x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setObject(String parameterName, Object x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        // TODO Auto-generated method stub

    }

    public String getString(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean getBoolean(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public byte getByte(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public short getShort(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getInt(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getLong(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public float getFloat(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public double getDouble(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public byte[] getBytes(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Date getDate(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Time getTime(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getObject(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Ref getRef(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Blob getBlob(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Clob getClob(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Array getArray(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public URL getURL(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public RowId getRowId(int parameterIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public RowId getRowId(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setRowId(String parameterName, RowId x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNString(String parameterName, String value) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNClob(String parameterName, NClob value) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public NClob getNClob(int parameterIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public NClob getNClob(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        // TODO Auto-generated method stub

    }

    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public SQLXML getSQLXML(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNString(int parameterIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNString(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Reader getNCharacterStream(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public Reader getCharacterStream(String parameterName) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setBlob(String parameterName, Blob x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setClob(String parameterName, Clob x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setClob(String parameterName, Reader reader) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        // TODO Auto-generated method stub

    }

    public void setNClob(String parameterName, Reader reader) throws SQLException {
        // TODO Auto-generated method stub

    }

    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}
