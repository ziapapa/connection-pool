package connectionpool;

import java.sql.Connection;

public interface ConnectionProvider {
    public abstract Connection getConnection() throws Exception;

    public abstract void returnConnection(Connection paramConnection) throws Exception;
}
