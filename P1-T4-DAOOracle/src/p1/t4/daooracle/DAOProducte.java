package p1.t4.daooracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import p1t4idao.IDAOProducte;
import p1t4model.Producte;

public class DAOProducte implements IDAOProducte {

    private Connection getConnection() throws Exception {
        return DBConfig.getConnection();
    }

    public boolean inserirProducte(Producte prod) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            String sqlItem = "INSERT INTO ITEM (codi_item, es_producte, nom, descripcio, stock, foto) VALUES ('"
                    + prod.getCodi() + "', 's', '"
                    + prod.getNom() + "', '"
                    + (prod.getDescrip() != null ? prod.getDescrip() : "") + "', "
                    + prod.getStock() + ", EMPTY_BLOB())";
            stmt.executeUpdate(sqlItem);

            String sqlProd = "INSERT INTO PRODUCTE (codip) VALUES ('" + prod.getCodi() + "')";
            int res = stmt.executeUpdate(sqlProd);
            return res > 0;
        } catch (Exception e) {
            System.out.println("Error inserirProducte: " + e.getMessage());
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public boolean modificarProducte(Producte prod) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            String sqlItem = "UPDATE ITEM SET nom='" + prod.getNom() + "', descripcio='"
                    + (prod.getDescrip() != null ? prod.getDescrip() : "")
                    + "', stock=" + prod.getStock()
                    + " WHERE codi_item='" + prod.getCodi() + "'";
            int res = stmt.executeUpdate(sqlItem);
            return res > 0;
        } catch (Exception e) {
            System.out.println("Error modificarProducte: " + e.getMessage());
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public boolean eliminarProducte(String codi) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            stmt.executeUpdate("DELETE FROM PRODUCTE WHERE codip='" + codi + "'");
            int res = stmt.executeUpdate("DELETE FROM ITEM WHERE codi_item='" + codi + "'");
            return res > 0;
        } catch (Exception e) {
            System.out.println("Error eliminarProducte: " + e.getMessage());
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public Producte obtenirProductePerId(String codi) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            String sql = "SELECT codi_item, nom, descripcio, stock FROM ITEM WHERE codi_item='" + codi + "'";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return new Producte(
                        rs.getString("codi_item"),
                        's',
                        rs.getString("nom"),
                        "dummy",
                        rs.getInt("stock"),
                        rs.getString("descripcio")
                );
            }
        } catch (Exception e) {
            System.out.println("Error obtenirProductePerId: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return null;
    }

    public List<Producte> llistarProductes() {
        List<Producte> llista = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            String sql = "SELECT codi_item, nom, descripcio, stock FROM ITEM WHERE es_producte='s'";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Producte p = new Producte(
                        rs.getString("codi_item"),
                        's',
                        rs.getString("nom"),
                        "dummy",
                        rs.getInt("stock"),
                        rs.getString("descripcio")
                );
                llista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error llistarProductes: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return llista;
    }

    @Override
    public List<Producte> filtrarPerCategoria(String codiCategoria) {
        List<Producte> llista = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            String sql = "SELECT i.codi_item, i.nom, i.descripcio, i.stock " +
                         "FROM ITEM i " +
                         "JOIN FORMACIO_PRODUCTE fp ON i.codi_item = fp.codi_item " +
                         "WHERE fp.codi_producte = '" + codiCategoria + "'";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Producte p = new Producte(
                        rs.getString("codi_item"),
                        's',
                        rs.getString("nom"),
                        "dummy",
                        rs.getInt("stock"),
                        rs.getString("descripcio")
                );
                llista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error filtrarPerCategoria: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return llista;
    }

    @Override
    public List<Producte> filtrarPerProveidor(String codiProveidor) {
        List<Producte> llista = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            String sql = "SELECT i.codi_item, i.nom, i.descripcio, i.stock " +
                         "FROM ITEM i " +
                         "JOIN COMPONENT c ON i.codi_item = c.codi_com " +
                         "JOIN COMPRES cp ON c.codi_com = cp.codi_comp " +
                         "WHERE cp.codi_prov = '" + codiProveidor + "'";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Producte p = new Producte(
                        rs.getString("codi_item"),
                        's',
                        rs.getString("nom"),
                        "dummy",
                        rs.getInt("stock"),
                        rs.getString("descripcio")
                );
                llista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error filtrarPerProveidor: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return llista;
    }
}
