package connectionpool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;

public class Debugger {
    private final boolean DEBUG;
    private static boolean STATICDEBUG = false;
    private final String CLASSNAME;
    private static PrintStream outStream;
    private static PrintWriter out = null;

    public Debugger(String fileName, String className) {
        System.out.println("Disributed Debugger is initailizing ;)  ....");
        STATICDEBUG = true;
        this.DEBUG = true;
        this.CLASSNAME = className;
        try {
            outStream = new PrintStream(new FileOutputStream(fileName, true));

            out = new PrintWriter(outStream, true);
            out.println("");
            out.println(String.valueOf(String.valueOf("*****Logger initialized at ").concat(String.valueOf(new Date())))
                    .concat(String.valueOf(" *****")));

            out.println("");
            System.out.println(String.valueOf("Enterprise Log file located at: ").concat(String.valueOf(fileName)));
        } catch (Exception exp) {
            System.err.println(String.valueOf("Could not open log file:").concat(String.valueOf(fileName)));
            System.err.println(String.valueOf("Exception: ").concat(String.valueOf(exp)));
            System.err.println("No Messages will be logged");
            STATICDEBUG = false;
        }
    }

    public Debugger(String className, boolean DEBUG) {
        this.DEBUG = DEBUG;
        this.CLASSNAME = className;
    }

    public void writeException(Exception exp) {
        if ((this.DEBUG) && (STATICDEBUG)) {
            out.println(String
                    .valueOf(String.valueOf(
                            String.valueOf(String.valueOf("<message class='").concat(String.valueOf(this.CLASSNAME)))
                                    .concat(String.valueOf("' time-stamp='")))
                            .concat(String.valueOf(new Date())))
                    .concat(String.valueOf("'>")));

            out.println(String.valueOf(String.valueOf("<exception>").concat(String.valueOf(exp)))
                    .concat(String.valueOf("</exception>")));
            out.println("<stack-trace>");
            exp.printStackTrace(outStream);
            out.println("</stack-trace>");
            out.println("</message>");
            outStream.flush();
        }
    }

    public void print(String msg) {
        if ((this.DEBUG) && (STATICDEBUG)) {
            out.println(String
                    .valueOf(String.valueOf(
                            String.valueOf(String.valueOf("<message class='").concat(String.valueOf(this.CLASSNAME)))
                                    .concat(String.valueOf("' time-stamp='")))
                            .concat(String.valueOf(new Date())))
                    .concat(String.valueOf("'>")));

            out.println(msg);
            out.println("</message>");
            out.flush();
        }
    }

    void waitHere() {
        try {
            System.in.read();
        } catch (IOException ie) {
            System.err.println(String.valueOf("DEBUGGER:Failed while waiting for input ").concat(String.valueOf(ie)));
        }
    }
}
