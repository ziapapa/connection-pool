package connectionpool;

public interface ConnectionMonitor {
    public abstract String getOwner();

    public abstract long getConnectionObtainedTime();

    public abstract long getLastAccessedTime();

    public abstract boolean isCurrentlyInTransaction();
}
