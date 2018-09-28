package connectionpool;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ConnectionPoolException extends SQLException {
    private Throwable previousException = null;
    protected String message = null;
    private Throwable nextException;
    Debugger debug = new Debugger("connectionpool.ConnectionPoolException", true);

    ConnectionPoolException() {
    }

    ConnectionPoolException(String messageId) {
        super(messageId);
        this.message = messageId;
        this.debug.print(String.valueOf("Exception Created:").concat(String.valueOf(messageId)));
    }

    ConnectionPoolException(String messageId, Throwable prevException) {
        super(messageId);
        if (((prevException instanceof ConnectionPoolException)) || (this.message == null)) {
            this.message = messageId;
        }
        this.previousException = prevException;
        this.debug.print(String.valueOf("Exception Created: ").concat(String.valueOf(messageId)));
        this.debug.writeException(this);
    }

    Throwable getPreviousException() {
        return this.previousException;
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (this.previousException != null) {
            System.err.println("Caused by Prev: ");
            this.previousException.printStackTrace();
        }
        if (this.nextException != null) {
            System.err.println("Caused by Next: ");
            this.nextException.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream ps) {
        if (ps == null) {
            System.err.println("PrintStream passed to printStackTrace method is null.");
            printStackTrace();
        }
        super.printStackTrace(ps);
        if (this.previousException != null) {
            ps.println("Caused by:");
            this.previousException.printStackTrace(ps);
        }
    }

    public String getMessageId() {
        if (this.message == null) {
            return null;
        }
        int index = this.message.indexOf(":");
        int len = this.message.length();
        if (index <= 0) {
            return null;
        }
        return this.message.substring(0, index);
    }

    String getRootMessageId() {
        ConnectionPoolException exp = null;
        Throwable prevExp = this;
        while ((prevExp != null) && ((prevExp instanceof ConnectionPoolException))) {
            exp = (ConnectionPoolException) prevExp;
            prevExp = exp.previousException;
        }
        if (exp == null) {
            return null;
        }
        String message = exp.message;
        if (message == null) {
            return null;
        }
        int index = message.indexOf(":");
        int len = message.length();
        if (index <= 0) {
            return null;
        }
        return message.substring(0, index);
    }

    public void printStackTrace(PrintWriter pw) {
        if (pw == null) {
            System.err.println("PrintWriter passed to printStackTrace method is null.");
            printStackTrace();
        }
        super.printStackTrace(pw);
        if (this.previousException != null) {
            pw.println("Caused by:");
            this.previousException.printStackTrace(pw);
        }
    }
}
