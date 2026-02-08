package p1.t4.daooracle;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Classe per gestionar la configuració de la base de dades.
 * Carrega la configuració des de db.properties si existeix,
 * altrament usa valors per defecte.
 */
public class DBConfig {
    
    private static Properties props = null;
    private static boolean initialized = false;
    
    static {
        carregarConfiguracio();
    }
    
    private static void carregarConfiguracio() {
        if (initialized) return;
        
        props = new Properties();
        try {
            // Intentar carregar des de diferents ubicacions
            InputStream is = DBConfig.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            
            if (is == null) {
                is = DBConfig.class.getClassLoader()
                        .getResourceAsStream("conf/db.properties");
            }
            
            if (is == null) {
                is = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("db.properties");
            }
            
            if (is == null) {
                is = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("conf/db.properties");
            }
            
            if (is != null) {
                props.load(is);
                is.close();
                System.out.println("DBConfig: Configuracio de BD carregada correctament des de db.properties");
            } else {
                System.out.println("DBConfig: No s'ha trobat db.properties, usant valors per defecte");
                props.setProperty("db.driver", "oracle.jdbc.driver.OracleDriver");
                props.setProperty("db.url", "jdbc:oracle:thin:@localhost:1521/XEPDB1");
                props.setProperty("db.user", "alumne");
                props.setProperty("db.password", "alumne");
            }
            
            // Carregar el driver
            String driver = props.getProperty("db.driver", "oracle.jdbc.driver.OracleDriver");
            Class.forName(driver);
            
            initialized = true;
            
        } catch (Exception e) {
            System.err.println("DBConfig: Error carregant configuracio de BD: " + e.getMessage());
            e.printStackTrace();
            // Usar valors per defecte en cas d'error
            props.setProperty("db.url", "jdbc:oracle:thin:@localhost:1521/XEPDB1");
            props.setProperty("db.user", "alumne");
            props.setProperty("db.password", "alumne");
        }
    }
    
    /**
     * Obté una connexió a la base de dades.
     * @return Connection objecte de connexió
     * @throws Exception si hi ha error de connexió
     */
    public static Connection getConnection() throws Exception {
        if (!initialized) {
            carregarConfiguracio();
        }
        
        return DriverManager.getConnection(
            props.getProperty("db.url"),
            props.getProperty("db.user"),
            props.getProperty("db.password")
        );
    }
    
    /**
     * Tanca una connexió de forma segura.
     * @param conn connexió a tancar
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                System.err.println("DBConfig: Error tancant connexio: " + e.getMessage());
            }
        }
    }
    
    /**
     * Obté l'URL de la base de dades.
     */
    public static String getUrl() {
        return props.getProperty("db.url");
    }
    
    /**
     * Obté l'usuari de la base de dades.
     */
    public static String getUser() {
        return props.getProperty("db.user");
    }
    
    /**
     * Obté la contrasenya de la base de dades.
     */
    public static String getPassword() {
        return props.getProperty("db.password");
    }
}