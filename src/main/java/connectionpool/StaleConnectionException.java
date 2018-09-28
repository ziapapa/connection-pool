package connectionpool;

public class StaleConnectionException extends ConnectionPoolException {
    public StaleConnectionException() {
    }

    public StaleConnectionException(String msg) {
        super(msg);
    }

    public String toString() {
        return String.valueOf("StaleConnectionException: ").concat(String.valueOf(this.message));
    }
}
