package p1.t4.daooracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import p1t4idao.IDAOProvincia;
import p1t4model.Provincia;

public class DAOProvincia implements IDAOProvincia {


    public List<Provincia> llistarProvincies() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Provincia> llista = new ArrayList<>();
        try{
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM PROVINCIA");
            while(rs.next()){
                Provincia p = new Provincia(
                    rs.getString("CODI_PROV"),
                    rs.getString("NOM")
                );
                llista.add(p);
            }
        } catch(Exception e){
            System.out.println("Error llistarProvincies: " + e.getMessage());
        } finally{
            try{ if(rs != null) rs.close(); } catch(Exception e){}
            try{ if(stmt != null) stmt.close(); } catch(Exception e){}
            try{ if(conn != null) conn.close(); } catch(Exception e){}
        }
        return llista;
    }

    public Provincia obtenirProvinciaPerId(String codiProv) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM PROVINCIA WHERE CODI_PROV='" + codiProv + "'");
            if(rs.next()){
                return new Provincia(
                    rs.getString("CODI_PROV"),
                    rs.getString("NOM")
                );
            }
        } catch(Exception e){
            System.out.println("Error obtenirProvinciaPerId: " + e.getMessage());
        } finally{
            try{ if(rs != null) rs.close(); } catch(Exception e){}
            try{ if(stmt != null) stmt.close(); } catch(Exception e){}
            try{ if(conn != null) conn.close(); } catch(Exception e){}
        }
        return null;
    }
}
