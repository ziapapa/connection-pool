package connectionpool;

import java.io.File;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

public class PoolManagerImpl implements PoolManager {
    private Hashtable poolMap = new Hashtable();
    private String defaultPool;
    private static final String className = "PoolManagerImpl";
    private static Debugger debug = new Debugger("PoolManagerImpl", true);

    private void loadConfig(File file) throws ConnectionPoolException {
        Collection c = new ConfigFileParser().getPoolConfig(file);
        Iterator it = c.iterator();
        while (it.hasNext()) {
            PoolConfig pc = (PoolConfig) it.next();
            String poolName = pc.getPoolName();
            debug.print(String.valueOf("").concat(String.valueOf(pc)));
            if (pc.isDefaultPool()) {
                if (this.defaultPool != null) {
                    throw new ConnectionPoolException(
                            "More than one Connection Pools cannot have default set to 'true'");
                }
                this.defaultPool = poolName;
            }
            this.poolMap.put(poolName, new ConnectionPool(pc));
        }
    }

    public PoolManagerImpl(String fileName) throws ConnectionPoolException {
        if ((fileName == null) || (fileName.trim().equals(""))) {
            throw new IllegalArgumentException("File Name cannot be null/empty");
        }
        File f1 = new File(fileName);
        loadConfig(f1);
    }

    public PoolManagerImpl(File file) throws ConnectionPoolException {
        loadConfig(file);
    }

    public Connection getConnection() throws ConnectionPoolException {
        if (this.defaultPool == null) {
            throw new ConnectionPoolException("No default pool specified");
        }
        return getConnection(this.defaultPool);
    }

    public Connection getConnection(String poolName) throws ConnectionPoolException {
        Pool p = (Pool) this.poolMap.get(poolName);
        if (p == null) {
            throw new ConnectionPoolException(String.valueOf("No such pool:").concat(String.valueOf(poolName)));
        }
        return p.getConnection();
    }

    public Connection getConnection(String poolName, String owner) throws ConnectionPoolException {
        Pool p = (Pool) this.poolMap.get(poolName);
        if (p == null) {
            throw new ConnectionPoolException(String.valueOf("No such pool:").concat(String.valueOf(poolName)));
        }
        return p.getConnection(owner);
    }

    public void addConnectionLeakListener(String poolName, ConnectionLeakListener cle) throws ConnectionPoolException {
        Pool p = (Pool) this.poolMap.get(poolName);
        if (p == null) {
            throw new ConnectionPoolException(String.valueOf("No such pool:").concat(String.valueOf(poolName)));
        }
        p.addConnectionLeakListener(cle);
    }

    public void removeConnectionLeakListener(String poolName, ConnectionLeakListener cle)
            throws ConnectionPoolException {
        Pool p = (Pool) this.poolMap.get(poolName);
        if (p == null) {
            throw new ConnectionPoolException(String.valueOf("No such pool:").concat(String.valueOf(poolName)));
        }
        p.removeConnectionLeakListener(cle);
    }

    public PoolMonitor getPoolMonitor(String poolName) throws ConnectionPoolException {
        PoolMonitor p = (PoolMonitor) this.poolMap.get(poolName);
        if (p == null) {
            throw new ConnectionPoolException(String.valueOf("No such pool:").concat(String.valueOf(poolName)));
        }
        return p;
    }

    public static void main(String[] args) throws Exception {
        int k = 0;
        Thread[] arr = new Thread[40];
        try {
            PoolManagerImpl p1 = new PoolManagerImpl("c:\\windows\\desktop\\test.xml");
            System.exit(0);
            for (int i = 0; i < 20; i++) {
                Thread r = new Thread(
                        new ThreadRunner(p1, "Sachin", String.valueOf("").concat(String.valueOf(i)), i * 800));

                arr[k] = r;
                k++;
                r.start();
                r = new Thread(new ThreadRunner(p1, "Shetty", String.valueOf("").concat(String.valueOf(i)), i * 800));

                r.start();
                arr[k] = r;
                k++;
            }
        } catch (Exception e) {
            debug.writeException(e);
        }
        for (int z = 0; z < k; z++) {
            System.out.println(String.valueOf("Wating ------> for ").concat(String.valueOf(z)));
            arr[z].join();
        }
        System.out.println("Child threads Finished -->Exiting");
    }

    public static class ThreadRunner implements Runnable {
        PoolManager p1;
        String poolName;
        String owner;
        int sleepTime;

        public ThreadRunner(PoolManager p1, String poolName, String owner, int sleepTime) {
            this.p1 = p1;
            this.poolName = poolName;
            this.owner = owner;
            this.sleepTime = sleepTime;
        }

        public void run() {
            try {
                for (int i = 0; i <= 10; i++) {
                    System.out.println(String.valueOf(String.valueOf("Thread ").concat(String.valueOf(this.owner)))
                            .concat(String.valueOf(" running")));
                    Connection conn = this.p1.getConnection(this.poolName);
                    try {
                        Thread.sleep(this.sleepTime);
                    } catch (InterruptedException localInterruptedException) {
                    }
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
