package connectionpool;

public interface ConfigMonitor {
    public abstract String getPoolName();

    public abstract int getMaxConnections();

    public abstract int getMinConnections();

    public abstract int getIncrement();

    public abstract String getUserName();

    public abstract String getPassword();

    public abstract String getConnectionString();

    public abstract String getDriver();

    public abstract boolean isDetectLeaks();

    public abstract boolean isDefaultPool();

    public abstract long getLeakTimeOut();

    public abstract String getDefaultListener();

    public abstract long getPollThreadTime();

    public abstract boolean isAutoClose();

    public abstract boolean isAllowAnonymousConnections();

    public abstract int getMaxConnectionsForRelease();

    public abstract long getConnectionWaitTimeOut();

    public abstract String getConnectionLoaderClass();

    public abstract String getValidatorQuery();

    public abstract long getMaxConnectionIdleTime();
}
