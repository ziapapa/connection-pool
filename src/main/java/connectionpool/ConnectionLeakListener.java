package connectionpool;

public interface ConnectionLeakListener {
    public abstract void connectionTimeOut(ConnectionLeakEvent paramConnectionLeakEvent);
}
