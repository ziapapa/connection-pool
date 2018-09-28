package connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ConnectionPool implements Pool, PoolMonitor {
    private Hashtable connectionHash = new Hashtable();
    private PoolConfig config;
    private final String name;
    private final Debugger debug;
    private int currentPoolSize = 0;
    private int usedConnections = 0;
    private Vector connectionList;
    private Vector connectionListenerList;
    private Thread pollerThread;
    private ConnectionProvider connProvider = null;
    private String validatorQuery = null;

    private Connection loadConnection() throws ConnectionPoolException {
        try {
            Class.forName(this.config.getDriver());
        } catch (ClassNotFoundException classNotFound) {
            throw new ConnectionPoolException("Could not load Driver", classNotFound);
        }
        Connection con = null;
        try {
            con = DriverManager.getConnection(this.config.getConnectionString(), this.config.getUserName(),
                    this.config.getPassword());
        } catch (Exception e) {
            throw new ConnectionPoolException("Could not obtain Connection", e);
        }
        this.currentPoolSize += 1;

        return con;
    }

    public Vector getConnectionsInUse() {
        return this.connectionList;
    }

    public Vector getConnectionLeakListeners() {
        return this.connectionListenerList;
    }

    public int getNoOfFreeConnections() {
        return this.currentPoolSize - this.usedConnections;
    }

    public ConfigMonitor getConfigMonitor() {
        return this.config;
    }

    private void initialiseConnections() throws ConnectionPoolException {
        try {
            int minConnections = this.config.getMinConnections();
            for (int i = 0; i < minConnections; i++) {
                this.connectionHash.put(loadConnection(), Boolean.TRUE);
            }
        } catch (Exception e) {
            throw new ConnectionPoolException("Could not load initial connection", e);
        }
    }

    private void returnConnectionToOtherPool(Connection conn) throws Exception {
        this.connProvider.returnConnection(conn);
    }

    ConnectionPool(PoolConfig config) throws ConnectionPoolException {
        this.config = config;
        try {
            if (config.getConnectionLoaderClass() != null) {
                this.connProvider = ((ConnectionProvider) getClass().getClassLoader()
                        .loadClass(config.getConnectionLoaderClass()).newInstance());
            }
        } catch (Exception exp) {
            throw new ConnectionPoolException("Error loading Connection Loader Class", exp);
        }
        if (this.connProvider == null) {
            initialiseConnections();
        }
        this.name = String.valueOf("ConnectionPool-").concat(String.valueOf(config.getPoolName()));
        this.debug = new Debugger(this.name, true);
        this.connectionList = new Vector(config.getMinConnections(), config.getIncrement());
        this.connectionListenerList = new Vector();
        try {
            if (config.getDefaultListener() != null) {
                String defaultListener = config.getDefaultListener();
                addConnectionLeakListener(
                        (ConnectionLeakListener) getClass().getClassLoader().loadClass(defaultListener).newInstance());
            }
        } catch (Exception e) {
            throw new ConnectionPoolException(
                    String.valueOf("Could not load class ").concat(String.valueOf(config.getDefaultListener())), e);
        }
        if (config.isDetectLeaks()) {
            this.pollerThread = new Thread(
                    new ConnectionLeakPollThread(this.connectionList, this.connectionListenerList, config.getPoolName(),
                            config.getPollThreadTime(), config.getLeakTimeOut(), this));

            this.pollerThread.setDaemon(true);
            this.pollerThread.start();
        }
        this.validatorQuery = config.getValidatorQuery();
    }

    public int getCurrentPoolSize() {
        return this.currentPoolSize;
    }

    public Connection getConnection() throws ConnectionPoolException {
        if (this.config.isAllowAnonymousConnections()) {
            return getConnection("N/A");
        }
        throw new ConnectionPoolException(
                "You are not allowed to take anonumous connections, please provide an owner name");
    }

    private boolean checkIfValid(Connection conn) {
        boolean bool;
        try {
            this.debug.print(String
                    .valueOf(String.valueOf(" Checking Connection for '").concat(String.valueOf(this.validatorQuery)))
                    .concat(String.valueOf("'")));
            Statement stmt;
            if ((this.validatorQuery != null) && (!this.validatorQuery.trim().equals(""))) {
                stmt = conn.createStatement();
                bool = stmt.execute(this.validatorQuery);
                stmt.close();
                this.debug.print(String.valueOf("Connection valiadation returning ").concat(String.valueOf(bool)));
                return bool;
            }
            this.debug.print("Connection valiadation disabled, returning true");
            return true;
        } catch (SQLException exp) {
            this.debug.print("Exception occuured in Validation returning false");
            this.debug.writeException(exp);
            bool = false;
        }
        return bool;
    }

    private Connection getConnectionFromOtherPool(String owner) throws ConnectionPoolException {
        try {
            ConnectionPool localConnectionPool = this;
            try {
                if (this.config.getMaxConnections() == this.usedConnections) {
                    try {
                        this.debug.print(String.valueOf("Hey the value is ")
                                .concat(String.valueOf(this.config.getConnectionWaitTimeOut())));

                        wait(this.config.getConnectionWaitTimeOut());
                        if (this.config.getMaxConnections() == this.usedConnections) {
                            throw new TimeOutException("Timed-out while waiting for free connection");
                        }
                    } catch (InterruptedException localInterruptedException) {
                    }
                }
                Connection conn = this.connProvider.getConnection();
                this.usedConnections += 1;
                this.currentPoolSize += 1;
                if (checkIfValid(conn)) {
                    SmartConnection smt = new SmartConnection(conn, this, owner, this.config.isAutoClose());

                    this.connectionList.add(smt);
                    return smt;
                }
                boolean valid = false;
                int i = 1;
                while (!valid) {
                    conn = this.connProvider.getConnection();
                    valid = checkIfValid(conn);
                    i++;
                    if ((i == 3) && (!valid)) {
                        throw new ConnectionPoolException(
                                "Three consecutive cnnections failes the Validator Query test");
                    }
                }
                SmartConnection smt = new SmartConnection(conn, this, owner, this.config.isAutoClose());

                this.connectionList.add(smt);
                return smt;
            } finally {
            }
        } catch (ConnectionPoolException cpe) {
            throw cpe;
        } catch (Exception exp) {
            throw new ConnectionPoolException("Error while getting connections from the Connection Loader Class", exp);
        }
    }

    public Connection getConnection(String owner) throws ConnectionPoolException {
        if (this.connProvider != null) {
            return getConnectionFromOtherPool(owner);
        }
        Enumeration cons = this.connectionHash.keys();

        Hashtable localHashtable = this.connectionHash;
        try {
            if (this.config.getMaxConnections() == this.usedConnections) {
                try {
                    this.debug.print(String.valueOf("Hey the value is ")
                            .concat(String.valueOf(this.config.getConnectionWaitTimeOut())));
                    this.connectionHash.wait(this.config.getConnectionWaitTimeOut());
                    if (this.config.getMaxConnections() == this.usedConnections) {
                        throw new TimeOutException("Timed-out while waiting for free connection");
                    }
                } catch (InterruptedException localInterruptedException) {
                }
            }
            while (cons.hasMoreElements()) {
                Connection con = (Connection) cons.nextElement();
                Boolean b = (Boolean) this.connectionHash.get(con);
                if (b == Boolean.TRUE) {
                    this.connectionHash.put(con, Boolean.FALSE);
                    this.usedConnections += 1;
                    this.debug.print(String.valueOf("Hey After Incrementing conn ")
                            .concat(String.valueOf(this.usedConnections)));
                    if (checkIfValid(con)) {
                        SmartConnection smt = new SmartConnection(con, this, owner, this.config.isAutoClose());

                        this.connectionList.add(smt);
                        return smt;
                    }
                    boolean valid = false;
                    int failCounter = 1;
                    while (!valid) {
                        this.connectionHash.remove(con);
                        con = loadConnection();
                        this.connectionHash.put(con, Boolean.FALSE);
                        failCounter++;
                        valid = checkIfValid(con);
                        if ((failCounter == 3) && (!valid)) {
                            throw new ConnectionPoolException(
                                    "Three consecutive connections failed the Validator Query test");
                        }
                    }
                    SmartConnection smt = new SmartConnection(con, this, owner, this.config.isAutoClose());

                    this.connectionList.add(smt);
                    return smt;
                }
            }
            int increment = this.config.getIncrement();
            Connection c = null;
            SmartConnection smt = null;
            for (int i = 0; (i < increment) && (i + this.currentPoolSize <= this.config.getMaxConnections()); i++) {
                c = loadConnection();
                boolean valid = checkIfValid(c);
                int failCounter = 1;
                while (!valid) {
                    c = loadConnection();
                    failCounter++;
                    valid = checkIfValid(c);
                    if ((failCounter == 3) && (!valid)) {
                        throw new ConnectionPoolException(
                                "Three consecutive connections failed the Validator Query test");
                    }
                }
                if (i == 0) {
                    smt = new SmartConnection(c, this, owner, this.config.isAutoClose());

                    this.connectionHash.put(c, Boolean.FALSE);
                } else {
                    this.connectionHash.put(c, Boolean.TRUE);
                }
            }
            this.usedConnections += 1;
            this.connectionList.add(smt);
            return smt;
        } finally {
        }
    }

    public void returnConnection(Connection ret) {
        if (this.connProvider != null) {
            try {
                ConnectionPool localConnectionPool = this;
                try {
                    Connection conn = ((SmartConnection) ret).returnConnection();
                    returnConnectionToOtherPool(conn);
                    this.usedConnections -= 1;
                    this.currentPoolSize -= 1;
                    this.debug.print(String.valueOf("Removed value is ")
                            .concat(String.valueOf(this.connectionList.removeElement(ret))));
                    notifyAll();
                } finally {
                }
            } catch (Exception exp) {
                this.debug.print(String.valueOf("Error ").concat(String.valueOf(exp)));
            }
        } else {
            Object tempRef = ret;
            SmartConnection smt = (SmartConnection) ret;
            ret = smt.returnConnection();

            Enumeration cons = this.connectionHash.keys();
            Hashtable localHashtable = this.connectionHash;
            try {
                while (cons.hasMoreElements()) {
                    Connection con = (Connection) cons.nextElement();
                    if (con == ret) {
                        this.connectionHash.put(con, Boolean.TRUE);
                        break;
                    }
                }
                this.debug.print(String.valueOf("Connection Released ").concat(String.valueOf(this.usedConnections)));
                this.usedConnections -= 1;
                this.debug.print(String.valueOf("Connection contains list ")
                        .concat(String.valueOf(this.connectionList.contains(tempRef))));
                this.debug.print(String.valueOf("Removed value is ")
                        .concat(String.valueOf(this.connectionList.removeElement(tempRef))));

                this.connectionHash.notifyAll();
            } finally {
            }
        }
    }

    public void addConnectionLeakListener(ConnectionLeakListener cle) throws ConnectionPoolException {
        if (cle == null) {
            throw new IllegalArgumentException("ConnectionLeakListener cannot be null");
        }
        this.debug.print(String.valueOf("Added is ").concat(String.valueOf(cle)));
        this.connectionListenerList.add(cle);
    }

    public void removeConnectionLeakListener(ConnectionLeakListener cle) throws ConnectionPoolException {
        if (cle == null) {
            throw new IllegalArgumentException("ConnectionLeakListener cannot be null");
        }
        this.debug.print(String.valueOf("Trying to remove ").concat(String.valueOf(cle)));
        boolean found = this.connectionListenerList.remove(cle);
        if (!found) {
            throw new ConnectionPoolException("No Such Listener");
        }
    }

    public void releaseConnections() {
        if (this.config.getMaxConnectionsForRelease() == -1) {
            return;
        }
        if (this.config.getMaxConnectionsForRelease() < getNoOfFreeConnections()) {
            int i = this.config.getIncrement();
            Hashtable localHashtable = this.connectionHash;
            try {
                Enumeration cons = this.connectionHash.keys();
                while ((cons.hasMoreElements()) && (i > 0)) {
                    Connection con = (Connection) cons.nextElement();
                    Boolean b = (Boolean) this.connectionHash.get(con);
                    if (b == Boolean.TRUE) {
                        this.connectionHash.remove(con);
                        try {
                            con.close();
                            i--;
                            this.currentPoolSize = this.connectionHash.size();
                            this.debug.print(String.valueOf("Releasing conn").concat(String.valueOf(con)));
                        } catch (SQLException e) {
                            this.debug.print(String.valueOf("Error in closing connection").concat(String.valueOf(e)));
                        }
                    }
                }
            } finally {
            }
        }
    }

    protected void finalize() throws Throwable {
        this.debug.print(String.valueOf("Finally Called releasing ").concat(String.valueOf(this.usedConnections)));
        Enumeration cons = this.connectionHash.keys();
        while (cons.hasMoreElements()) {
            Connection con = (Connection) cons.nextElement();
            try {
                con.close();
            } catch (Exception localException) {
            }
        }
    }
}
