package p1.t4.daooracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOBom {

    private Connection getConnection() throws Exception {
        return DBConfig.getConnection();
    }

    public boolean afegir(String codiProducte, String codiItem, int quantitat) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "INSERT INTO FORMACIO_PRODUCTE (codi_producte, codi_item, quantitat) VALUES (?, ?, ?)");
            ps.setString(1, codiProducte);
            ps.setString(2, codiItem);
            ps.setInt(3, quantitat);
            int files = ps.executeUpdate();
            return files > 0;
        } catch (Exception e) {
            System.out.println("Error afegir BOM: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
    }

    public boolean eliminar(String codiProducte, String codiItem) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "DELETE FROM FORMACIO_PRODUCTE WHERE codi_producte=? AND codi_item=?");
            ps.setString(1, codiProducte);
            ps.setString(2, codiItem);
            int files = ps.executeUpdate();
            return files > 0;
        } catch (Exception e) {
            System.out.println("Error eliminar BOM: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
    }

    public boolean modificarQuantitat(String codiProducte, String codiItem, int novaQuantitat) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "UPDATE FORMACIO_PRODUCTE SET quantitat=? WHERE codi_producte=? AND codi_item=?");
            ps.setInt(1, novaQuantitat);
            ps.setString(2, codiProducte);
            ps.setString(3, codiItem);
            int files = ps.executeUpdate();
            return files > 0;
        } catch (Exception e) {
            System.out.println("Error modificar quantitat BOM: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
    }

    public boolean existeix(String codiProducte, String codiItem) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT 1 FROM FORMACIO_PRODUCTE WHERE codi_producte=? AND codi_item=?");
            ps.setString(1, codiProducte);
            ps.setString(2, codiItem);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Error comprovar existència BOM: " + e.getMessage());
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
    }

    public List<String[]> obtenirBom(String codiProducte) {
        List<String[]> llista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT i.codi_item, i.nom, i.es_producte, fp.quantitat " +
                    "FROM FORMACIO_PRODUCTE fp " +
                    "JOIN ITEM i ON fp.codi_item = i.codi_item " +
                    "WHERE fp.codi_producte=? " +
                    "ORDER BY i.es_producte DESC, i.nom");
            ps.setString(1, codiProducte);
            rs = ps.executeQuery();
            while (rs.next()) {
                llista.add(new String[]{
                        rs.getString("codi_item"),
                        rs.getString("nom"),
                        rs.getString("es_producte"),
                        String.valueOf(rs.getInt("quantitat"))
                });
            }
        } catch (Exception e) {
            System.out.println("Error obtenirBom: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
        return llista;
    }

    public List<String[]> obtenirItemsDisponibles(String codiProducte) {
        List<String[]> llista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT i.codi_item, i.nom, i.es_producte " +
                    "FROM ITEM i " +
                    "WHERE i.codi_item != ? " +
                    "AND i.codi_item NOT IN (SELECT codi_item FROM FORMACIO_PRODUCTE WHERE codi_producte=?) " +
                    "ORDER BY i.es_producte DESC, i.nom");
            ps.setString(1, codiProducte);
            ps.setString(2, codiProducte);
            rs = ps.executeQuery();
            while (rs.next()) {
                llista.add(new String[]{
                        rs.getString("codi_item"),
                        rs.getString("nom"),
                        rs.getString("es_producte")
                });
            }
        } catch (Exception e) {
            System.out.println("Error obtenirItemsDisponibles: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConfig.closeConnection(con);
        }
        return llista;
    }
    
}