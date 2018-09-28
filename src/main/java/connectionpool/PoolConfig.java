package connectionpool;

public class PoolConfig implements ConfigMonitor {
    private String poolName;
    private int maxConnections;
    private int minConnections;
    private int increment;
    private String userName;
    private String password;
    private String connectionString;
    private String driver;
    private boolean isDefaultPool = false;
    private boolean detectLeaks = false;
    private long leakTimeOut = -1L;
    private String defaultListener;
    private long pollThreadTime;
    private boolean autoClose;
    private int maxConnectionsForRelease = -1;
    private boolean allowAnonymousConnections;
    private boolean externalPooling;
    private String connectionLoaderClass = null;
    private long connectionWaitTimeOut = 60000L;
    private String validatorQuery = null;
    private long maxConnectionIdleTime = -1L;

    PoolConfig(String poolName, int maxConnections, int minConnections, String userName, String password,
            String connectionString, int increment, String driver) {
        this.poolName = poolName;
        this.maxConnections = maxConnections;
        this.minConnections = minConnections;
        this.increment = increment;
        this.userName = userName;
        this.password = password;
        this.connectionString = connectionString;
        this.driver = driver;
    }

    public String getPoolName() {
        return this.poolName;
    }

    void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getMaxConnections() {
        return this.maxConnections;
    }

    void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMinConnections() {
        return this.minConnections;
    }

    void setMinConnections(int minConnections) {
        this.minConnections = minConnections;
    }

    public int getIncrement() {
        return this.increment;
    }

    void setIncrement(int increment) {
        this.increment = increment;
    }

    public String getUserName() {
        return this.userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public String getConnectionString() {
        return this.connectionString;
    }

    void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getDriver() {
        return this.driver;
    }

    void setDriver(String driver) {
        this.driver = driver;
    }

    public boolean isDefaultPool() {
        return this.isDefaultPool;
    }

    void setDefaultPool(boolean b) {
        this.isDefaultPool = b;
    }

    public boolean isDetectLeaks() {
        return this.detectLeaks;
    }

    void setDetectLeaks(boolean b) {
        this.detectLeaks = b;
    }

    public long getLeakTimeOut() {
        return this.leakTimeOut;
    }

    void setLeakTimeOut(long leakTimeOut) {
        this.leakTimeOut = leakTimeOut;
    }

    public String getDefaultListener() {
        return this.defaultListener;
    }

    void setDefaultListener(String defaultListener) {
        this.defaultListener = defaultListener;
    }

    public long getPollThreadTime() {
        return this.pollThreadTime;
    }

    void setPollThreadTime(long pollThreadTime) {
        this.pollThreadTime = pollThreadTime;
    }

    public boolean isAutoClose() {
        return this.autoClose;
    }

    void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    public int getMaxConnectionsForRelease() {
        return this.maxConnectionsForRelease;
    }

    void setMaxConnectionsForRelease(int maxConnectionsForRelease) {
        this.maxConnectionsForRelease = maxConnectionsForRelease;
    }

    void setAllowAnonymousConnections(boolean allowAnonymousConnections) {
        this.allowAnonymousConnections = allowAnonymousConnections;
    }

    public boolean isAllowAnonymousConnections() {
        return this.allowAnonymousConnections;
    }

    void setConnectionLoaderClass(String connectionLoaderClass) {
        this.connectionLoaderClass = connectionLoaderClass;
    }

    public String getConnectionLoaderClass() {
        return this.connectionLoaderClass;
    }

    public boolean isExternalPooling() {
        return this.externalPooling;
    }

    void setExternalPooling(boolean externalPooling) {
        this.externalPooling = externalPooling;
    }

    public long getConnectionWaitTimeOut() {
        return this.connectionWaitTimeOut;
    }

    void setConnectionWaitTimeOut(long connectionWaitTimeOut) {
        this.connectionWaitTimeOut = connectionWaitTimeOut;
    }

    public long getMaxConnectionIdleTime() {
        return this.maxConnectionIdleTime;
    }

    void setMaxConnectionIdleTime(long maxConnectionIdleTime) {
        this.maxConnectionIdleTime = maxConnectionIdleTime;
    }

    void setValidatorQuery(String validatorQuery) {
        this.validatorQuery = validatorQuery;
    }

    public String getValidatorQuery() {
        return this.validatorQuery;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n\nConnection Pool Configuration ======>");
        sb.append(String.valueOf("\n\tPoolName: ").concat(String.valueOf(this.poolName)));
        sb.append(String.valueOf("\n\tMax Connections: ").concat(String.valueOf(this.maxConnections)));
        sb.append(String.valueOf("\n\tMin Connections: ").concat(String.valueOf(this.minConnections)));
        sb.append(String.valueOf("\n\tIncrement-Connections by: ").concat(String.valueOf(this.increment)));
        sb.append(String.valueOf("\n\tUser Name: ").concat(String.valueOf(this.userName)));
        sb.append(String.valueOf("\n\tPassword: ").concat(String.valueOf(this.password)));
        sb.append(String.valueOf("\n\tConnection String: ").concat(String.valueOf(this.connectionString)));
        sb.append(String.valueOf("\n\tDriver: ").concat(String.valueOf(this.driver)));
        sb.append(String.valueOf("\n\tVakidator Query: ").concat(String.valueOf(this.validatorQuery)));
        sb.append(String.valueOf("\n\tDefault Pool: ").concat(String.valueOf(this.isDefaultPool)));
        sb.append(String.valueOf("\n\tDetect Leaks: ").concat(String.valueOf(this.detectLeaks)));
        sb.append(String.valueOf(String.valueOf("\n\tLeak Timeout: ").concat(String.valueOf(this.leakTimeOut / 'Ϩ')))
                .concat(String.valueOf(" Seconds")));
        sb.append(String.valueOf("\n\tDefault Listener: ").concat(String.valueOf(this.defaultListener)));
        sb.append(String
                .valueOf(String.valueOf("\n\tPool Thread Time: ").concat(String.valueOf(this.pollThreadTime / 'Ϩ')))
                .concat(String.valueOf(" Seconds")));
        sb.append(String.valueOf("\n\tAuto Close: ").concat(String.valueOf(this.autoClose)));
        sb.append(String.valueOf("\n\tMax connection for release: ")
                .concat(String.valueOf(this.maxConnectionsForRelease)));
        sb.append(String.valueOf("\n\tConnection Loader Class: ").concat(String.valueOf(this.connectionLoaderClass)));
        sb.append(String.valueOf(String.valueOf("\n\tConnection Wait Time Out: ")
                .concat(String.valueOf(this.connectionWaitTimeOut / 'Ϩ'))).concat(String.valueOf(" Seconds")));
        sb.append(String.valueOf(String.valueOf("\n\tMaximum Connection Idle Time: ")
                .concat(String.valueOf(this.maxConnectionIdleTime / 'Ϩ'))).concat(String.valueOf(" Seconds")));

        return sb.toString();
    }
}
