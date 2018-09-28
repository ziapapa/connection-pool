package ziapapa.connectionpool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class ConnPool {
    private Properties dbProperties;
    private Vector waitingPool;
    private Vector usingPool;

    private String driverName;
    private String dbURL;
    private String dbUser;
    private String dbPasswd;

    private int init_connections;
    private int max_connections;
    private int connections_created;
    private long wait_time;
    private boolean initFromProperties = true;

    private String path = "e:/jtomcat/chapters/chap09/jsp/prop/db.properties";

    public ConnPool() {
    }

    public void setInitFromProperties(boolean init) {
        initFromProperties = init;
    }

    public void setPath(String path) {
        if (path == null)
            return; // if null use default!
        this.path = path;
    }

    public void setUp() throws SQLException, ClassNotFoundException, IOException {
        if (initFromProperties) {
            init();
        }
        Class.forName(this.driverName);
        System.out.println("Driver " + this.driverName + " Loaded successfully");
        if (max_connections <= 0) {
            max_connections = 5;
        }
        if (max_connections < init_connections) {
            init_connections = max_connections;
        }
        waitingPool = new Vector(init_connections);
        usingPool = new Vector(init_connections);
        for (int i = 0; i < init_connections; i++) {
            addConnection();
        }
    }

    public Connection getConnection() throws SQLException {
        System.out.println("connections available: " + waitingPool.size());
        System.out.println("total connections made: " + (waitingPool.size() + usingPool.size()));
        return getConnection(wait_time);
    }

    public synchronized void releaseConnection(Connection connection) {
        notify();
        usingPool.removeElement(connection);
        connections_created--;
        waitingPool.addElement(connection);
        connections_created++;
        System.out.println("Connection released");
    }

    public synchronized void closeAll() {

        Enumeration cons = ((Vector) waitingPool.clone()).elements();
        while (cons.hasMoreElements()) {
            Connection connection = (Connection) cons.nextElement();
            waitingPool.removeElement(connection);
            connections_created--;
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }

        cons = ((Vector) usingPool.clone()).elements();
        while (cons.hasMoreElements()) {
            Connection connection = (Connection) cons.nextElement();
            usingPool.removeElement(connection);
        }
        System.out.println("Closed All connections in waiting pool and disable all active connections");
    }

    public int getMaxConnections() {
        return max_connections;
    }

    public long getWaitTime() {
        return wait_time;
    }

    public String getDbURL() {
        return dbURL;
    }

    public String getDbPasswd() {
        return this.dbPasswd;
    }

    public String getDbUser() {
        return this.dbUser;
    }

    public int getInitConnections() {
        return this.init_connections;
    }

    public String getPath() {
        return path;
    }

    public void setInitConnections(int init_connections) {
        this.init_connections = init_connections;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public void setDbPasswd(String dbPasswd) {
        this.dbPasswd = dbPasswd;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public void setWaitTime(long wait_time) {
        this.wait_time = wait_time;
    }

    public void setMaxConnections(int max_connections) {
        this.max_connections = max_connections;
    }

    ////////////////// Utility Methods ////////////////////
    private synchronized Connection getConnection(long wait_time) throws SQLException {
        if (waitingPool.isEmpty()) {
            if (connections_created < max_connections) {
                addConnection();
            } else {
                long start = System.currentTimeMillis();
                long elapsedTime = 0;
                try {
                    while (waitingPool.isEmpty() && connections_created >= max_connections) {
                        this.wait(wait_time);
                        elapsedTime = System.currentTimeMillis() - start;
                        wait_time -= elapsedTime;
                        if (wait_time <= 0)
                            break;
                    }
                } catch (InterruptedException e) {
                }
                if (waitingPool.isEmpty()) {
                    if (connections_created < max_connections) {
                        addConnection();
                    } else {
                        throw new SQLException("wait_time for a connection to be released expired");
                    }
                }
            }
        } // end of outer-most if clause
        Connection connection;
        synchronized (usingPool) {
            connection = (Connection) waitingPool.lastElement();
            waitingPool.removeElement(connection);
            usingPool.addElement(connection);
        }
        return connection;
    }

    private void addConnection() throws SQLException {
        waitingPool.addElement(createConnection());
    }

    private Connection createConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbURL, dbUser, dbPasswd);
            connection.setAutoCommit(false);
            System.out.println("Successfully created a new connection from " + dbURL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connections_created++;
        return connection;
    }

    private void init() throws IOException {
        dbProperties = new Properties();
        try {
            File filePath = new File(getPath());
            FileInputStream fin = new FileInputStream(filePath);
            dbProperties.load(fin);
        } catch (IOException e) {
            throw e;
        }
        this.dbURL = dbProperties.getProperty("dbURL");
        this.driverName = dbProperties.getProperty("driverName");
        this.dbUser = dbProperties.getProperty("dbUser");
        this.dbPasswd = dbProperties.getProperty("dbPasswd");
        this.init_connections = Integer.parseInt(dbProperties.getProperty("init_connections"));
        this.max_connections = Integer.parseInt(dbProperties.getProperty("max_connections"));
        this.wait_time = Long.parseLong(dbProperties.getProperty("wait_time"));
    }
    ////////////////// Utility Methods ////////////////////
}