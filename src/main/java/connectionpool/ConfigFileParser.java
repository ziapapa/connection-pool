package connectionpool;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigFileParser extends DefaultHandler {
    private ArrayList ar = new ArrayList();
    private static final String className = "ConfigFileParser";
    private HashMap hs = new HashMap();
    private boolean defaultPool = false;
    private static Debugger debug = null;
    private String lastElement = null;
    private Stack stack = new Stack();
    private String contents = null;

    Collection getPoolConfig(String fileName) throws ConnectionPoolException {
        if ((fileName == null) || (fileName.equals(""))) {
            throw new IllegalArgumentException("File Name cannot be null/empty");
        }
        return getPoolConfig(new File(fileName));
    }

    Collection getPoolConfig(File file) throws ConnectionPoolException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file, this);
            return this.ar;
        } catch (Exception t) {
            throw new ConnectionPoolException(String.valueOf("Could not parse file:").concat(String.valueOf(file)), t);
        }
    }

    public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException {
        this.stack.push(qName);
        if (!qName.equals("data-source")) {
            return;
        }
        String poolName = attrs.getValue("pool-name");
        if (this.hs.get(poolName) != null) {
            throw new SAXException(String.valueOf("Duplicate connection pool name:").concat(String.valueOf(poolName)));
        }
        this.hs.put(poolName, "");

        String temp = attrs.getValue("default-pool");
        boolean newDefaultPool = false;
        if (temp != null) {
            newDefaultPool = temp.equals("true");
        } else {
            newDefaultPool = false;
        }
        if ((this.defaultPool) && (newDefaultPool)) {
            throw new SAXException("More than one pools with default-pool setting");
        }
        if (newDefaultPool) {
            this.defaultPool = newDefaultPool;
        }
        temp = attrs.getValue("detect-leaks");
        boolean detectLeaks = false;
        if (temp != null) {
            detectLeaks = temp.equals("true");
        } else {
            detectLeaks = false;
        }
        temp = attrs.getValue("leak-time-out");
        long leakTimeOut = 0L;
        if (temp != null) {
            leakTimeOut = Long.parseLong(temp) * 'Ϩ';
        } else {
            leakTimeOut = 300000L;
        }
        temp = attrs.getValue("poll-thread-time");
        long pollThreadTime = 0L;
        if (temp != null) {
            pollThreadTime = Long.parseLong(temp) * 'Ϩ';
        } else {
            pollThreadTime = 300000L;
        }
        temp = attrs.getValue("auto-close");
        boolean autoClose = false;
        if (temp != null) {
            autoClose = temp.equals("true");
        } else {
            autoClose = false;
        }
        String defaultListener = attrs.getValue("default-listener");

        int maxConnectionsForRelease = -1;
        temp = attrs.getValue("max-free-connections-for-release");
        if (temp != null) {
            try {
                maxConnectionsForRelease = Integer.parseInt(temp);
            } catch (NumberFormatException ne) {
                throw new SAXException("max-free-connections-for-release is non numeric", ne);
            }
        }
        temp = attrs.getValue("allow-anonymous-connections");
        boolean allowAnonymousConnections = false;
        if (temp != null) {
            allowAnonymousConnections = temp.equals("true");
        } else {
            allowAnonymousConnections = false;
        }
        String connectionLoaderClass = null;
        temp = attrs.getValue("connection-loader-class");
        if (temp != null) {
            connectionLoaderClass = temp;
        }
        long connectionWaitTimeOut = 60000L;
        temp = attrs.getValue("connection-wait-time-out");
        if (temp != null) {
            connectionWaitTimeOut = Long.parseLong(temp) * 'Ϩ';
        }
        String validatorQuery = "";
        temp = attrs.getValue("validator-query");
        if (temp != null) {
            validatorQuery = temp;
        }
        long maxConnectionIdleTime = -1L;
        temp = attrs.getValue("max-connection-idle-time");
        if (temp != null) {
            maxConnectionIdleTime = Long.parseLong(temp) * 'Ϩ';
        }
        PoolConfig config = new PoolConfig(attrs.getValue("pool-name"),
                Integer.parseInt(attrs.getValue("max-connections")),
                Integer.parseInt(attrs.getValue("min-connections")), attrs.getValue("username"),
                attrs.getValue("password"), attrs.getValue("connect-string"),
                Integer.parseInt(attrs.getValue("increment-by")), attrs.getValue("connection-driver"));

        config.setValidatorQuery(validatorQuery);
        config.setDefaultPool(newDefaultPool);
        config.setDetectLeaks(detectLeaks);
        config.setLeakTimeOut(leakTimeOut);
        config.setDefaultListener(attrs.getValue("default-listener"));
        config.setPollThreadTime(pollThreadTime);
        config.setAutoClose(autoClose);
        if (defaultListener != null) {
            config.setDefaultListener(defaultListener);
        }
        config.setMaxConnectionsForRelease(maxConnectionsForRelease);
        config.setAllowAnonymousConnections(allowAnonymousConnections);
        config.setConnectionLoaderClass(connectionLoaderClass);
        config.setConnectionWaitTimeOut(connectionWaitTimeOut);
        config.setMaxConnectionIdleTime(maxConnectionIdleTime);
        this.ar.add(config);
    }

    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        if (qName.equals("log-file")) {
            String fileName = (String) this.stack.pop();
            debug = new Debugger(this.contents, "ConfigFileParser");
        }
    }

    public static void main(String[] args) throws ConnectionPoolException {
        Collection s = new ConfigFileParser().getPoolConfig("/home/sssachin/test.xml");

        Iterator it = s.iterator();
        while (it.hasNext()) {
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        this.contents = new String(ch, start, length);
    }
}
