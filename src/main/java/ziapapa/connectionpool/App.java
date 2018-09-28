package ziapapa.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        Logger logger = LoggerFactory.getLogger("ziapapa.connectionpool.App");

        logger.trace("Hello world");
        logger.debug("Hello world");
        logger.info("Hello world");
        logger.warn("Hello world");
        logger.error("Hello world");

        Connection conn = DriverManager.getConnection("jdbc:mysql://mdb.test.wecandeo.com:3306/test?characterEncoding=utf-8&amp;useOldAliasMetadataBehavior=true&amp;autoReconnect=true", "devel", "devel9870");

        Statement stmt = conn.createStatement();
        String sql = "select * from items";
        ResultSet rs = stmt.executeQuery(sql);

        String a = "";
        while (rs.next()) {
            a = rs.getString(1);
            System.out.println(a);
        }

        
        


    }
}
