<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.*"%>
<%
    String codiProducte = request.getParameter("codiProducte");
    String nomProducte = "";
    String urlPDF = "http://localhost:8080/jasperserver/rest_v2/reports/Reports/Blank_A4.pdf?codiProducte=" + codiProducte;
    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    List<String[]> bomItems = new java.util.ArrayList<>();
    
    try {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XEPDB1", "alumne", "alumne");
        
        // Obtenir nom del producte
        ps = con.prepareStatement("SELECT nom FROM ITEM WHERE codi_item = ?");
        ps.setString(1, codiProducte);
        rs = ps.executeQuery();
        if (rs.next()) {
            nomProducte = rs.getString("nom");
        }
        rs.close();
        ps.close();
        
        // Obtenir BOM
        ps = con.prepareStatement(
            "SELECT fp.codi_item, i.nom, i.es_producte, fp.quantitat " +
            "FROM FORMACIO_PRODUCTE fp " +
            "JOIN ITEM i ON fp.codi_item = i.codi_item " +
            "WHERE fp.codi_producte = ? " +
            "ORDER BY i.es_producte DESC, i.nom"
        );
        ps.setString(1, codiProducte);
        rs = ps.executeQuery();
        while (rs.next()) {
            bomItems.add(new String[]{
                rs.getString("codi_item"),
                rs.getString("nom"),
                rs.getString("es_producte"),
                String.valueOf(rs.getInt("quantitat"))
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {}
    }
%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <title>BOM - <%= nomProducte %></title>
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Playfair+Display:wght@700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Open Sans', sans-serif;
            background-color: #f6f3eb;
            min-height: 100vh;
        }
        .header {
            background-color: #1B263B;
            color: white;
            padding: 20px 25px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .header h1 {
            font-family: 'Playfair Display', serif;
            font-size: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .header h1 .material-icons {
            color: #D4AF37;
        }
        .header-info {
            text-align: right;
        }
        .header-info .codi {
            color: #D4AF37;
            font-weight: 600;
            font-size: 16px;
        }
        .header-info .nom {
            font-size: 14px;
            opacity: 0.9;
        }
        .content {
            padding: 20px 25px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        th {
            background-color: #1B263B;
            color: white;
            padding: 12px 15px;
            text-align: left;
            font-weight: 600;
            font-size: 14px;
        }
        td {
            padding: 12px 15px;
            border-bottom: 1px solid #eee;
            font-size: 14px;
        }
        tr:last-child td {
            border-bottom: none;
        }
        tr:hover {
            background-color: #f9f9f9;
        }
        .tipus {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            padding: 4px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
        }
        .tipus.producte {
            background-color: #dbeafe;
            color: #1e40af;
        }
        .tipus.component {
            background-color: #fef3c7;
            color: #92400e;
        }
        .tipus .material-icons {
            font-size: 14px;
        }
        .quantitat {
            font-weight: 600;
            color: #1B263B;
            text-align: center;
        }
        .no-items {
            text-align: center;
            padding: 40px;
            color: #666;
            background: white;
            border-radius: 8px;
        }
        .no-items .material-icons {
            font-size: 48px;
            color: #ccc;
            display: block;
            margin-bottom: 10px;
        }
        .footer {
            padding: 20px 25px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .total {
            font-weight: 600;
            color: #666;
            font-size: 14px;
        }
        .actions {
            display: flex;
            gap: 10px;
        }
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            font-size: 14px;
            font-weight: 600;
            text-decoration: none;
            transition: background-color 0.3s;
        }
        .btn-download {
            background-color: #dc2626;
            color: white;
        }
        .btn-download:hover {
            background-color: #b91c1c;
        }
        .btn-close {
            background-color: #6B7280;
            color: white;
        }
        .btn-close:hover {
            background-color: #4B5563;
        }
    </style>
</head>
<body>

<div class="header">
    <h1><span class="material-icons">account_tree</span> Bill of Materials (BOM)</h1>
    <div class="header-info">
        <div class="codi"><%= codiProducte %></div>
        <div class="nom"><%= nomProducte %></div>
    </div>
</div>

<div class="content">
    <% if (!bomItems.isEmpty()) { %>
    <table>
        <thead>
            <tr>
                <th>Codi</th>
                <th>Nom</th>
                <th>Tipus</th>
                <th style="text-align:center">Quantitat</th>
            </tr>
        </thead>
        <tbody>
            <% for (String[] item : bomItems) { %>
            <tr>
                <td><%= item[0] %></td>
                <td><%= item[1] %></td>
                <td>
                    <span class="tipus <%= item[2].equals("s") ? "producte" : "component" %>">
                        <span class="material-icons"><%= item[2].equals("s") ? "inventory_2" : "settings" %></span>
                        <%= item[2].equals("s") ? "Producte" : "Component" %>
                    </span>
                </td>
                <td class="quantitat"><%= item[3] %></td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <% } else { %>
    <div class="no-items">
        <span class="material-icons">inbox</span>
        Aquest producte no té components assignats
    </div>
    <% } %>
</div>

<div class="footer">
    <div class="total">
        <% if (!bomItems.isEmpty()) { %>
            Total: <%= bomItems.size() %> elements
        <% } %>
    </div>
    <div class="actions">
        <a href="<%= urlPDF %>" target="_blank" class="btn btn-download">
            <span class="material-icons">picture_as_pdf</span> Descarregar PDF
        </a>
        <button onclick="window.close()" class="btn btn-close">
            <span class="material-icons">close</span> Tancar
        </button>
    </div>
</div>

</body>
</html>