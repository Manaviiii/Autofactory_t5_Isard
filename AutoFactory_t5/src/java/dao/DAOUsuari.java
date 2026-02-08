package dao;

import java.sql.*;
import util.DBConnection;

public class DAOUsuari {
    
    public String getPasswordHash(String username) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            System.out.println("DAOUsuari: Intentant obtenir password per a: " + username);
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT password FROM USUARIS WHERE username = ? AND actiu = 's'"
            );
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                String hash = rs.getString("password");
                System.out.println("DAOUsuari: Password hash trobat per a: " + username);
                return hash;
            } else {
                System.out.println("DAOUsuari: No s'ha trobat l'usuari o no està actiu: " + username);
            }
            
        } catch (Exception e) {
            System.err.println("DAOUsuari: Error obtenint password per a " + username + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConnection.closeConnection(con);
        }
        
        return null;
    }
    
    public String getNomComplet(String username) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT nom_complet FROM USUARIS WHERE username = ?"
            );
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getString("nom_complet");
            }
            
        } catch (Exception e) {
            System.err.println("DAOUsuari: Error obtenint nom: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConnection.closeConnection(con);
        }
        
        return null;
    }
    
    public String getRol(String username) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT rol FROM USUARIS WHERE username = ?"
            );
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getString("rol");
            }
            
        } catch (Exception e) {
            System.err.println("DAOUsuari: Error obtenint rol: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            DBConnection.closeConnection(con);
        }
        
        return null;
    }
}