package connectionpool;

import java.sql.Connection;

public class ConnectionLeakEventImpl implements ConnectionLeakEvent {
    private Connection conn;
    private String owner;
    private long lastAccessedTime;
    private long connectionObtainedTime;
    private String poolName;

    ConnectionLeakEventImpl(Connection conn, String owner, long lastAccessedTime, long connectionObtainedTime,
            String poolName) {
        this.conn = conn;
        this.owner = owner;
        this.lastAccessedTime = lastAccessedTime;
        this.connectionObtainedTime = connectionObtainedTime;
        this.poolName = poolName;
    }

    public Connection getConnection() {
        return this.conn;
    }

    public String getOwner() {
        return this.owner;
    }

    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    public long getConnectionObtainedTime() {
        return this.connectionObtainedTime;
    }

    public String getPoolName() {
        return this.poolName;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nConnectionLeakEvent--------->");
        sb.append(String.valueOf("\n\tConnection: ").concat(String.valueOf(this.conn)));
        sb.append(String.valueOf("\n\tOwner: ").concat(String.valueOf(this.owner)));
        sb.append(String.valueOf("\n\tlastAccessedTime:  ").concat(String.valueOf(this.lastAccessedTime)));
        sb.append(String.valueOf("\n\tconnectionObtainedTime :  ").concat(String.valueOf(this.connectionObtainedTime)));
        sb.append(String.valueOf("\n\tpoolName :  ").concat(String.valueOf(this.poolName)));

        return sb.toString();
    }
}
