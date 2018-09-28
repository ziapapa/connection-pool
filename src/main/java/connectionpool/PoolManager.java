package connectionpool;

import java.sql.Connection;

public interface PoolManager {
    public abstract Connection getConnection() throws ConnectionPoolException;

    public abstract Connection getConnection(String paramString) throws ConnectionPoolException;

    public abstract Connection getConnection(String paramString1, String paramString2) throws ConnectionPoolException;

    public abstract void addConnectionLeakListener(String paramString,
            ConnectionLeakListener paramConnectionLeakListener) throws ConnectionPoolException;

    public abstract void removeConnectionLeakListener(String paramString,
            ConnectionLeakListener paramConnectionLeakListener) throws ConnectionPoolException;

    public abstract PoolMonitor getPoolMonitor(String paramString) throws ConnectionPoolException;
}
