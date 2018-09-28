package connectionpool;

import java.util.Vector;

class ConnectionLeakPollThread implements Runnable {
    private Vector connectionsInUse;
    private Vector connectionListenerList;
    private String poolName;
    private long sleepTime;
    private boolean keepGoing;
    private long leakTimeOut;
    private static Debugger debug;
    private Pool pool;

    ConnectionLeakPollThread(Vector connectionsInUse, Vector connectionListenerList, String poolName, long sleepTime,
            long leakTimeOut, Pool pool) {
        this.connectionsInUse = connectionsInUse;
        this.connectionListenerList = connectionListenerList;
        this.poolName = poolName;
        this.sleepTime = sleepTime;
        this.leakTimeOut = leakTimeOut;
        this.keepGoing = true;
        this.pool = pool;
        debug = new Debugger(String.valueOf("ConnectionLeakPollThread-").concat(String.valueOf(poolName)), true);
    }

    private void notifyAll(SmartConnection sConn) {
        for (int i = 0; i < this.connectionListenerList.size(); i++) {
            ConnectionLeakEvent cle = new ConnectionLeakEventImpl(sConn, sConn.getOwner(), sConn.getLastAccessedTime(),
                    sConn.getConnectionObtainedTime(), this.poolName);

            ConnectionLeakListener c = (ConnectionLeakListener) this.connectionListenerList.get(i);

            c.connectionTimeOut(cle);
        }
    }

    private void checkAndRelease() {
        this.pool.releaseConnections();
    }

    private void checkAndNotify() {
        for (int i = 0; i < this.connectionsInUse.size(); i++) {
            SmartConnection sConn = (SmartConnection) this.connectionsInUse.get(i);
            if (System.currentTimeMillis() - sConn.getConnectionObtainedTime() >= this.leakTimeOut) {
                notifyAll(sConn);
            }
            if (System.currentTimeMillis() - sConn.getLastAccessedTime() > ((PoolMonitor) this.pool).getConfigMonitor()
                    .getMaxConnectionIdleTime()) {
                debug.print("Found a Idle Connection ...");
                sConn.forcedClose();
            }
        }
    }

    public void run() {
        debug.print("Starting Thread for detecting connection leaks");
        while (this.keepGoing) {
            checkAndNotify();
            checkAndRelease();
            try {
                Thread.sleep(this.sleepTime);
            } catch (Exception localException) {
            }
        }
    }

    private void stop() {
        this.keepGoing = false;
    }
}
