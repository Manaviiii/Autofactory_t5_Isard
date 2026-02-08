package p1.t4.daooracle;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import p1t4idao.IDAOMesura;
import p1t4model.UnitatMesura;

public class DAOMesura implements IDAOMesura {

    public List<UnitatMesura> llistarMesures() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<UnitatMesura> llista = new ArrayList<>();
        try {
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT CODI_MESURA, NOM_UNITAT FROM UNITAT_MESURA");
            while (rs.next()) {
                UnitatMesura u = new UnitatMesura(
                    rs.getString("CODI_MESURA"),
                    rs.getString("NOM_UNITAT")
                );
                llista.add(u);
            }
        } catch (Exception e) {
            System.out.println("Error llistarMesures: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            DBConfig.closeConnection(conn);
        }
        return llista;
    }

    public UnitatMesura obtenirMesuraPerId(String id) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT CODI_MESURA, NOM_UNITAT FROM UNITAT_MESURA WHERE CODI_MESURA='" + id + "'");
            if (rs.next()) {
                return new UnitatMesura(
                    rs.getString("CODI_MESURA"),
                    rs.getString("NOM_UNITAT")
                );
            }
        } catch (Exception e) {
            System.out.println("Error obtenirMesuraPerId: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            DBConfig.closeConnection(conn);
        }
        return null;
    }
}
