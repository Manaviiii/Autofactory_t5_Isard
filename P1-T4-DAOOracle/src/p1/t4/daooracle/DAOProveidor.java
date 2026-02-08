package p1.t4.daooracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import p1t4idao.IDAOProveidor;
import p1t4model.Proveidor;

public class DAOProveidor implements IDAOProveidor {


    public boolean eliminarProveidor(String codi) {
        Connection conn = null;
        Statement stmt = null;
        try{
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            int result = stmt.executeUpdate("DELETE FROM PROVEIDOR WHERE CODI='" + codi + "'");
            return result > 0;
        } catch(Exception e){
            System.out.println("Error eliminarProveidor: " + e.getMessage());
        } finally{
            try{ if(stmt != null) stmt.close(); } catch(Exception e){}
            try{ if(conn != null) conn.close(); } catch(Exception e){}
        }
        return false;
    }

    public boolean inserirProveidor(Proveidor p) {
        Connection conn = null;
        Statement stmt = null;
        try{
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            String sql = "INSERT INTO PROVEIDOR (CODI, CIF, RAO_SOCIAL, LINIA_ADRECA_FACTURACIO, MUNICIPI, PERSONA_CONTACTE, TELF_CONTACTE) VALUES (" +
                         "'" + p.getCodi() + "', " +
                         "'" + p.getCIF() + "', " +
                         "'" + p.getRaoSocial() + "', " +
                         "'" + p.getLiniaAdrecaFacturacio() + "', " +
                         "'" + p.getMunicipi() + "', " +
                         (p.getPersonaContacte() != null ? "'" + p.getPersonaContacte() + "'" : "NULL") + ", " +
                         (p.getTelfContacte() != null ? "'" + p.getTelfContacte() + "'" : "NULL") +
                         ")";
            int result = stmt.executeUpdate(sql);
            return result > 0;
        } catch(Exception e){
            System.out.println("Error inserirProveidor: " + e.getMessage());
        } finally{
            try{ if(stmt != null) stmt.close(); } catch(Exception e){}
            try{ if(conn != null) conn.close(); } catch(Exception e){}
        }
        return false;
    }

    public List<Proveidor> llistarProveidors() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Proveidor> llista = new ArrayList<>();
        try{
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM PROVEIDOR");
            while(rs.next()){
                Proveidor p = new Proveidor(
                    rs.getString("CODI"),
                    rs.getString("CIF"),
                    rs.getString("RAO_SOCIAL"),
                    rs.getString("LINIA_ADRECA_FACTURACIO"),
                    rs.getString("MUNICIPI"),
                    rs.getString("PERSONA_CONTACTE"),
                    rs.getString("TELF_CONTACTE")
                );
                llista.add(p);
            }
        } catch(Exception e){
            System.out.println("Error llistarProveidors: " + e.getMessage());
        } finally{
            try{ if(rs != null) rs.close(); } catch(Exception e){}
            try{ if(stmt != null) stmt.close(); } catch(Exception e){}
            try{ if(conn != null) conn.close(); } catch(Exception e){}
        }
        return llista;
    }

    public boolean modificarProveidor(Proveidor p) {
        Connection conn = null;
        Statement stmt = null;
        try{
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            String sql = "UPDATE PROVEIDOR SET " +
                         "CIF='" + p.getCIF() + "', " +
                         "RAO_SOCIAL='" + p.getRaoSocial() + "', " +
                         "LINIA_ADRECA_FACTURACIO='" + p.getLiniaAdrecaFacturacio() + "', " +
                         "MUNICIPI='" + p.getMunicipi() + "', " +
                         "PERSONA_CONTACTE=" + (p.getPersonaContacte() != null ? "'" + p.getPersonaContacte() + "'" : "NULL") + ", " +
                         "TELF_CONTACTE=" + (p.getTelfContacte() != null ? "'" + p.getTelfContacte() + "'" : "NULL") +
                         " WHERE CODI='" + p.getCodi() + "'";
            int result = stmt.executeUpdate(sql);
            return result > 0;
        } catch(Exception e){
            System.out.println("Error modificarProveidor: " + e.getMessage());
        } finally{
            try{ if(stmt != null) stmt.close(); } catch(Exception e){}
            try{ if(conn != null) conn.close(); } catch(Exception e){}
        }
        return false;
    }

    public Proveidor obtenirProveidorPerId(String codi) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            conn = DBConfig.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM PROVEIDOR WHERE CODI='" + codi + "'");
            if(rs.next()){
                return new Proveidor(
                    rs.getString("CODI"),
                    rs.getString("CIF"),
                    rs.getString("RAO_SOCIAL"),
                    rs.getString("LINIA_ADRECA_FACTURACIO"),
                    rs.getString("MUNICIPI"),
                    rs.getString("PERSONA_CONTACTE"),
                    rs.getString("TELF_CONTACTE")
                );
            }
        } catch(Exception e){
            System.out.println("Error obtenirProveidorPerId: " + e.getMessage());
        } finally{
            try{ if(rs != null) rs.close(); } catch(Exception e){}
            try{ if(stmt != null) stmt.close(); } catch(Exception e){}
            try{ if(conn != null) conn.close(); } catch(Exception e){}
        }
        return null;
    }
}
