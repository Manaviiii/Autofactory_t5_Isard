<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="p1t4model.Proveidor"%>
<%@page import="p1t4model.Municipi"%>
<%
    Proveidor prov = (Proveidor) request.getAttribute("proveidor");
    Boolean modeEdicio = (Boolean) request.getAttribute("modeEdicio");
    boolean editar = (modeEdicio != null && modeEdicio);
    List<Municipi> municipis = (List<Municipi>) request.getAttribute("municipis");
%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= editar ? "Editar Proveïdor" : "Nou Proveïdor" %> - AutoFactory</title>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/formProveidor.css">
</head>
<body>

<header>
    <a href="menu.jsp"><img src="img/logo_autofactory.png" alt="AutoFactory Logo"></a>
    <nav>
        <a href="menu.jsp">Home</a>
        <a href="ComponentServlet">Components</a>
        <a href="ProductServlet">Productes</a>
        <a href="ProveidorServlet">Proveïdors</a>
        <a href="ArbreServlet">Vista Arbre</a>
        <a href="LogoutServlet" class="logout">Logout</a>
    </nav>
</header>

<main>
    <h1><%= editar ? "Editar Proveïdor" : "Nou Proveïdor" %></h1>

    <form method="post" action="ProveidorServlet" id="proveidorForm">
        <input type="hidden" name="action" value="<%= editar ? "update" : "insert" %>">
        
        <div class="form-group">
            <label for="codi">Codi *</label>
            <% if (editar) { %>
                <input type="text" id="codi" value="<%= prov.getCodi() %>" readonly class="readonly">
                <input type="hidden" name="codi" value="<%= prov.getCodi() %>">
            <% } else { %>
                <input type="text" id="codi" name="codi" required maxlength="10" placeholder="Màxim 10 caràcters">
            <% } %>
        </div>

        <div class="form-group">
            <label for="cif">CIF *</label>
            <input type="text" id="cif" name="cif" required maxlength="20" 
                   value="<%= prov != null ? prov.getCIF() : "" %>" placeholder="Ex: B12345678">
        </div>

        <div class="form-group">
            <label for="raoSocial">Raó Social *</label>
            <input type="text" id="raoSocial" name="raoSocial" required maxlength="100" 
                   value="<%= prov != null ? prov.getRaoSocial() : "" %>">
        </div>

        <div class="form-group">
            <label for="adreca">Adreça de Facturació *</label>
            <input type="text" id="adreca" name="adreca" required maxlength="200" 
                   value="<%= prov != null ? prov.getLiniaAdrecaFacturacio() : "" %>">
        </div>

        <div class="form-group">
            <label for="municipi">Municipi *</label>
            <select id="municipi" name="municipi" required>
                <option value="">-- Selecciona --</option>
                <% if (municipis != null) {
                    for (Municipi m : municipis) { %>
                    <option value="<%= m.getCodiMun() %>" 
                        <%= prov != null && prov.getMunicipi().equals(m.getCodiMun()) ? "selected" : "" %>>
                        <%= m.getNom() %>
                    </option>
                <% }} %>
            </select>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="personaContacte">Persona de Contacte</label>
                <input type="text" id="personaContacte" name="personaContacte" maxlength="50" 
                       value="<%= prov != null && prov.getPersonaContacte() != null ? prov.getPersonaContacte() : "" %>">
            </div>

            <div class="form-group">
                <label for="telefon">Telèfon</label>
                <input type="text" id="telefon" name="telefon" maxlength="20" 
                       value="<%= prov != null && prov.getTelfContacte() != null ? prov.getTelfContacte() : "" %>" 
                       placeholder="Ex: 600123456">
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn-primary">
                <span class="material-icons">save</span> Desar
            </button>
            <button type="button" class="btn-secondary" onclick="location.href='ProveidorServlet'">
                <span class="material-icons">cancel</span> Cancel·lar
            </button>
        </div>
    </form>
</main>

<p id="author">Victor Villagra</p>

</body>
</html>