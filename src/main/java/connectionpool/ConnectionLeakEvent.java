package connectionpool;

import java.sql.Connection;

public interface ConnectionLeakEvent {
    public abstract Connection getConnection();

    public abstract String getOwner();

    public abstract long getLastAccessedTime();

    public abstract long getConnectionObtainedTime();

    public abstract String getPoolName();
}
