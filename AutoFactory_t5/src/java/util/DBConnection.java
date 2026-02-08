package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    
    private static Properties props = null;
    
    static {
        carregarConfiguracio();
    }
    
    private static void carregarConfiguracio() {
        props = new Properties();
        try {
            InputStream is = DBConnection.class.getClassLoader()
                    .getResourceAsStream("conf/db.properties");
            
            if (is != null) {
                props.load(is);
                is.close();
                System.out.println("DBConnection: Configuracio de BD carregada correctament");
                System.out.println("DBConnection: URL = " + props.getProperty("db.url"));
                System.out.println("DBConnection: User = " + props.getProperty("db.user"));
            } else {
                System.out.println("DBConnection: AVIS - No s'ha trobat db.properties, usant valors per defecte");
                props.setProperty("db.driver", "oracle.jdbc.driver.OracleDriver");
                props.setProperty("db.url", "jdbc:oracle:thin:@localhost:1521/XEPDB1");
                props.setProperty("db.user", "alumne");
                props.setProperty("db.password", "alumne");
            }
            
            Class.forName(props.getProperty("db.driver"));
            
        } catch (Exception e) {
            System.err.println("DBConnection: Error carregant configuracio de BD: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws Exception {
        if (props == null) {
            carregarConfiguracio();
        }
        
        try {
            Connection conn = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
            );
            System.out.println("DBConnection: Connexió establerta correctament");
            return conn;
        } catch (Exception e) {
            System.err.println("DBConnection: Error connectant a la BD:");
            System.err.println("  URL: " + props.getProperty("db.url"));
            System.err.println("  User: " + props.getProperty("db.user"));
            System.err.println("  Error: " + e.getMessage());
            throw e;
        }
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                System.err.println("DBConnection: Error tancant connexio: " + e.getMessage());
            }
        }
    }
}