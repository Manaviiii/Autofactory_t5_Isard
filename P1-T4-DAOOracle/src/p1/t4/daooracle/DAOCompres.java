package p1.t4.daooracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOCompres {

    private Connection getConnection() throws Exception {
        return DBConfig.getConnection();
    }

    public List<String[]> obtenirPreusComponent(String codiComponent) {
        List<String[]> llista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                "SELECT c.codi_prov, p.rao_social, c.preu " +
                "FROM COMPRES c " +
                "JOIN PROVEIDOR p ON c.codi_prov = p.codi " +
                "WHERE c.codi_comp = ? " +
                "ORDER BY c.preu ASC");
            ps.setString(1, codiComponent);
            rs = ps.executeQuery();
            while (rs.next()) {
                llista.add(new String[]{
                    rs.getString("codi_prov"),
                    rs.getString("rao_social"),
                    String.valueOf(rs.getDouble("preu"))
                });
            }
        } catch (Exception e) {
            System.out.println("Error obtenirPreusComponent: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
        return llista;
    }

    public boolean afegirPreu(String codiComponent, String codiProveidor, double preu) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                "INSERT INTO COMPRES (codi_comp, codi_prov, preu) VALUES (?, ?, ?)");
            ps.setString(1, codiComponent);
            ps.setString(2, codiProveidor);
            ps.setDouble(3, preu);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error afegirPreu: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
    }

    public boolean modificarPreu(String codiComponent, String codiProveidor, double nouPreu) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                "UPDATE COMPRES SET preu = ? WHERE codi_comp = ? AND codi_prov = ?");
            ps.setDouble(1, nouPreu);
            ps.setString(2, codiComponent);
            ps.setString(3, codiProveidor);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error modificarPreu: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
    }

    public boolean eliminarPreu(String codiComponent, String codiProveidor) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                "DELETE FROM COMPRES WHERE codi_comp = ? AND codi_prov = ?");
            ps.setString(1, codiComponent);
            ps.setString(2, codiProveidor);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error eliminarPreu: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
    }

    public boolean existeix(String codiComponent, String codiProveidor) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                "SELECT 1 FROM COMPRES WHERE codi_comp = ? AND codi_prov = ?");
            ps.setString(1, codiComponent);
            ps.setString(2, codiProveidor);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Error existeix: " + e.getMessage());
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
    }

    public List<String[]> obtenirProveidorsDisponibles(String codiComponent) {
        List<String[]> llista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                "SELECT p.codi, p.rao_social FROM PROVEIDOR p " +
                "WHERE p.codi NOT IN (SELECT codi_prov FROM COMPRES WHERE codi_comp = ?) " +
                "ORDER BY p.rao_social");
            ps.setString(1, codiComponent);
            rs = ps.executeQuery();
            while (rs.next()) {
                llista.add(new String[]{
                    rs.getString("codi"),
                    rs.getString("rao_social")
                });
            }
        } catch (Exception e) {
            System.out.println("Error obtenirProveidorsDisponibles: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
        return llista;
    }
}