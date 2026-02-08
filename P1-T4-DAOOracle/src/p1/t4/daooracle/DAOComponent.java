package p1.t4.daooracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import p1t4idao.IDAOComponent;
import p1t4model.Component;

public class DAOComponent implements IDAOComponent {

    private Connection getConnection() throws Exception {
        return DBConfig.getConnection();
    }

    public boolean crear(Component c) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("INSERT INTO ITEM (codi_item, es_producte, nom, descripcio, stock, foto) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, c.getCodi());
            ps.setString(2, String.valueOf(c.getEs_producte()));
            ps.setString(3, c.getNom());
            ps.setString(4, c.getDescrip());
            ps.setInt(5, c.getStock());
            ps.setBytes(6, new byte[]{0}); // dummy para BLOB
            ps.executeUpdate();

            ps = con.prepareStatement("INSERT INTO COMPONENT (codi_com, codi_fabricant, preu_mig, unitat_mesura) VALUES (?, ?, ?, ?)");
            ps.setString(1, c.getCodi());
            ps.setString(2, c.getCodiFabricant());
            ps.setInt(3, c.getPreuMig());
            ps.setString(4, c.getUnitatMesura());
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.out.println("Error en crear: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public boolean actualizar(Component c) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("UPDATE ITEM SET nom = ?, descripcio = ?, stock = ? WHERE codi_item = ?");
            ps.setString(1, c.getNom());
            ps.setString(2, c.getDescrip());
            ps.setInt(3, c.getStock());
            ps.setString(4, c.getCodi());
            ps.executeUpdate();

            ps = con.prepareStatement("UPDATE COMPONENT SET codi_fabricant = ?, unitat_mesura = ? WHERE codi_com = ?");
            ps.setString(1, c.getCodiFabricant());
            ps.setString(2, c.getUnitatMesura());
            ps.setString(3, c.getCodi());
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.out.println("Error en actualizar: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public boolean eliminar(String codiCom) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("DELETE FROM COMPONENT WHERE codi_com = ?");
            ps.setString(1, codiCom);
            ps.executeUpdate();

            ps = con.prepareStatement("DELETE FROM ITEM WHERE codi_item = ?");
            ps.setString(1, codiCom);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.out.println("Error en eliminar: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public Component buscarPorCodigo(String codiCom) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT i.codi_item, i.es_producte, i.nom, i.descripcio, i.stock, c.codi_fabricant, c.preu_mig, c.unitat_mesura " +
                    "FROM ITEM i JOIN COMPONENT c ON i.codi_item = c.codi_com WHERE i.codi_item = ?");
            ps.setString(1, codiCom);
            rs = ps.executeQuery();
            if (rs.next()) {
                Component c = new Component(
                        rs.getString("codi_item"),
                        rs.getString("es_producte").charAt(0),
                        rs.getString("nom"),
                        "dummy",
                        rs.getString("codi_fabricant"),
                        rs.getString("unitat_mesura")
                );
                c.setPreuMig(rs.getInt("preu_mig"));
                c.setStock(rs.getInt("stock"));
                c.setDescrip(rs.getString("descripcio"));
                return c;
            }
            return null;

        } catch (Exception e) {
            System.out.println("Error creando Component: " + e.getMessage());
            return null;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    public List<Component> listarTodos() {
        List<Component> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT i.codi_item, i.es_producte, i.nom, i.descripcio, i.stock, c.codi_fabricant, c.preu_mig, c.unitat_mesura " +
                    "FROM ITEM i JOIN COMPONENT c ON i.codi_item = c.codi_com");
            rs = ps.executeQuery();
            while (rs.next()) {
                Component c = new Component(
                        rs.getString("codi_item"),
                        rs.getString("es_producte").charAt(0),
                        rs.getString("nom"),
                        "dummy",
                        rs.getString("codi_fabricant"),
                        rs.getString("unitat_mesura")
                );
                c.setPreuMig(rs.getInt("preu_mig"));
                c.setStock(rs.getInt("stock"));
                c.setDescrip(rs.getString("descripcio"));
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error creando Component: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return lista;
    }

    public boolean actualizarFoto(String codiItem, byte[] foto) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("UPDATE ITEM SET foto = ? WHERE codi_item = ?");
            ps.setBytes(1, foto);
            ps.setString(2, codiItem);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            System.out.println("Error actualizant foto: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}