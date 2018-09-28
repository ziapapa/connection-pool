package connectionpool;

import java.sql.SQLException;

public interface Close {
    public abstract void close() throws SQLException;

    public abstract boolean isClosed() throws SQLException;
}
