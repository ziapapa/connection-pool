package connectionpool;

import java.sql.Connection;
import java.util.Vector;

public interface Pool {
    public abstract Connection getConnection() throws ConnectionPoolException;

    public abstract Connection getConnection(String paramString) throws ConnectionPoolException;

    public abstract int getCurrentPoolSize();

    public abstract void returnConnection(Connection paramConnection);

    public abstract Vector getConnectionsInUse();

    public abstract Vector getConnectionLeakListeners();

    public abstract int getNoOfFreeConnections();

    public abstract void addConnectionLeakListener(ConnectionLeakListener paramConnectionLeakListener)
            throws ConnectionPoolException;

    public abstract void removeConnectionLeakListener(ConnectionLeakListener paramConnectionLeakListener)
            throws ConnectionPoolException;

    public abstract void releaseConnections();
}
