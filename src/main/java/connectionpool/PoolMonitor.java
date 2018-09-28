package connectionpool;

import java.util.Vector;

public interface PoolMonitor {
    public abstract int getCurrentPoolSize();

    public abstract int getNoOfFreeConnections();

    public abstract Vector getConnectionsInUse();

    public abstract ConfigMonitor getConfigMonitor();
}
