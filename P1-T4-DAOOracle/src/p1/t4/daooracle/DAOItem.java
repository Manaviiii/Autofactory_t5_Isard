package p1.t4.daooracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import p1t4idao.IDAOItem;
import p1t4model.Item;

public class DAOItem implements IDAOItem {

    private Connection getConnection() throws Exception {
        return DBConfig.getConnection();
    }

    public boolean inserirItem(Item item) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            String sql = "INSERT INTO ITEM (codi_item, es_producte, nom, descripcio, stock, foto) VALUES ('"
                    + item.getCodi() + "', '"
                    + item.getEs_producte() + "', '"
                    + item.getNom() + "', '"
                    + (item.getDescrip() != null ? item.getDescrip() : "") + "', "
                    + item.getStock() + ", EMPTY_BLOB())";

            int res = stmt.executeUpdate(sql);
            return res > 0;
        } catch (Exception e) {
            System.out.println("Error inserirItem: " + e.getMessage());
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public boolean modificarItem(Item item) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            String sql = "UPDATE ITEM SET nom='" + item.getNom() + "', descripcio='"
                    + (item.getDescrip() != null ? item.getDescrip() : "")
                    + "', stock=" + item.getStock()
                    + " WHERE codi_item='" + item.getCodi() + "'";

            int res = stmt.executeUpdate(sql);
            return res > 0;
        } catch (Exception e) {
            System.out.println("Error modificarItem: " + e.getMessage());
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public boolean eliminarItem(String codi) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            String sql = "DELETE FROM ITEM WHERE codi_item='" + codi + "'";
            int res = stmt.executeUpdate(sql);
            return res > 0;
        } catch (Exception e) {
            System.out.println("Error eliminarItem: " + e.getMessage());
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public Item obtenirItemPerId(String codi) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            String sql = "SELECT codi_item, es_producte, nom, descripcio, stock FROM ITEM WHERE codi_item='" + codi + "'";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return new Item(
                        rs.getString("codi_item"),
                        rs.getString("es_producte").charAt(0),
                        rs.getString("nom"),
                        "dummy",
                        rs.getInt("stock"),
                        rs.getString("descripcio")
                );
            }
        } catch (Exception e) {
            System.out.println("Error obtenirItemPerId: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return null;
    }

    public List<Item> llistarItems() {
        List<Item> llista = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            stmt = con.createStatement();

            String sql = "SELECT codi_item, es_producte, nom, descripcio, stock FROM ITEM";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Item i = new Item(
                        rs.getString("codi_item"),
                        rs.getString("es_producte").charAt(0),
                        rs.getString("nom"),
                        "dummy",
                        rs.getInt("stock"),
                        rs.getString("descripcio")
                );
                llista.add(i);
            }
        } catch (Exception e) {
            System.out.println("Error llistarItems: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return llista;
    }

    @Override
    public List<Item> filtrarPerProducte(String codiProducte) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
