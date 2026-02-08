package p1.t4.daooracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import p1t4idao.IDAOMunicipi;
import p1t4model.Municipi;

public class DAOMunicipi implements IDAOMunicipi {


    public List<Municipi> llistarMunicipis() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Municipi> llista = new ArrayList<>();
        try{
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT CODI_MUN, CODI_PROV, NOM FROM MUNICIPI");
            while(rs.next()){
                Municipi m = new Municipi(
                    rs.getString("CODI_MUN"),
                    rs.getString("CODI_PROV"),
                    rs.getString("NOM")
                );
                llista.add(m);
            }
        } catch(Exception e){
            System.out.println("Error llistarMunicipis: " + e.getMessage());
        } finally{
            try{ if(rs != null) rs.close(); } catch(Exception e){}
            try{ if(stmt != null) stmt.close(); } catch(Exception e){}
            try{ if(conn != null) conn.close(); } catch(Exception e){}
        }
        return llista;
    }

    public Municipi obtenirMunicipiPerId(String codiMun) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT CODI_MUN, CODI_PROV, NOM FROM MUNICIPI WHERE CODI_MUN='" + codiMun + "'");
            if(rs.next()){
                return new Municipi(
                    rs.getString("CODI_MUN"),
                    rs.getString("CODI_PROV"),
                    rs.getString("NOM")
                );
            }
        } catch(Exception e){
            System.out.println("Error obtenirMunicipiPerId: " + e.getMessage());
        } finally{
            try{ if(rs != null) rs.close(); } catch(Exception e){}
            try{ if(stmt != null) stmt.close(); } catch(Exception e){}
            try{ if(conn != null) conn.close(); } catch(Exception e){}
        }
        return null;
    }
}
