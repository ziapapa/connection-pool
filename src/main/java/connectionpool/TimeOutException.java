package connectionpool;

public class TimeOutException extends ConnectionPoolException {
    public TimeOutException() {
    }

    public TimeOutException(String msg) {
        super(msg);
    }

    public String toString() {
        return String.valueOf("TimeOutException: ").concat(String.valueOf(this.message));
    }
}
