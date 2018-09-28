package connectionpool;

import java.io.File;
import java.sql.Connection;

public class SmartPoolFactory {
    private static PoolManager pm = null;

    private void startPool(File file) throws ConnectionPoolException {
        if (pm == null) {
            pm = new PoolManagerImpl(file);
        }
    }

    public SmartPoolFactory(File file) throws ConnectionPoolException {
        if (pm != null) {
            throw new ConnectionPoolException(
                    "This Class follows a Singleton Pattern. Instance has already been created and initialized.");
        }
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null");
        }
        startPool(file);
    }

    public SmartPoolFactory(String fileName) throws ConnectionPoolException {
        if (pm != null) {
            throw new ConnectionPoolException(
                    "This Class follows a Singleton Pattern. Instance has already been created and initialized.");
        }
        if ((fileName == null) || (fileName.trim().equals(""))) {
            throw new IllegalArgumentException("filename cannot be null/empty");
        }
        File file = new File(fileName);
        startPool(file);
    }

    private static void checkAndThrow() throws ConnectionPoolException {
        if (pm == null) {
            throw new ConnectionPoolException(
                    "PoolManager is not initialised ,Please create an object first with the configuration");
        }
    }

    public static Connection getConnection() throws ConnectionPoolException {
        checkAndThrow();

        return pm.getConnection();
    }

    public static Connection getConnection(String poolName) throws ConnectionPoolException {
        checkAndThrow();

        return pm.getConnection(poolName);
    }

    public static Connection getConnection(String poolName, String owner) throws ConnectionPoolException {
        checkAndThrow();

        return pm.getConnection(poolName, owner);
    }

    public static void addConnectionLeakListener(String poolName, ConnectionLeakListener cle)
            throws ConnectionPoolException {
        checkAndThrow();
        pm.addConnectionLeakListener(poolName, cle);
    }

    public static void removeConnectionLeakListener(String poolName, ConnectionLeakListener cle)
            throws ConnectionPoolException {
        checkAndThrow();
        pm.removeConnectionLeakListener(poolName, cle);
    }

    public static PoolMonitor getPoolMonitor(String poolName) throws ConnectionPoolException {
        checkAndThrow();

        return pm.getPoolMonitor(poolName);
    }

    public static void main(String[] arg) throws Exception {
        SmartPoolFactory smp = new SmartPoolFactory("c:\\windows\\desktop\\test.xml");
        Connection conn = getConnection();
    }
}
