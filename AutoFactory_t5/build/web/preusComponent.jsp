<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="p1t4model.Component"%>
<%@page import="p1t4model.Proveidor"%>
<%
    Component comp = (Component) request.getAttribute("component");
    List<String[]> preus = (List<String[]>) request.getAttribute("preus");
    List<Proveidor> proveidors = (List<Proveidor>) request.getAttribute("proveidors");
%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Preus per Proveïdor - AutoFactory</title>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/preusComponent.css">
</head>
<body>

<header>
    <a href="menu.jsp">
        <img src="img/logo_autofactory.png" alt="AutoFactory Logo">
    </a>
</header>

<main>
    <a href="ComponentServlet" class="back-link">
        <span class="material-icons">arrow_back</span> Tornar al llistat
    </a>

    <% if (comp != null) { %>
    <h1>Preus per Proveïdor</h1>
    <div class="component-info">
        <p><strong>Component:</strong> <%= comp.getNom() %> (<%= comp.getCodi() %>)</p>
        <p><strong>Preu Mig Actual:</strong> <%= comp.getPreuMig() %> €</p>
    </div>

    <%
        String msg = request.getParameter("msg");
        String error = request.getParameter("error");
        if (msg != null) {
    %>
        <div class="alert alert-success"><%= msg %></div>
    <% } %>
    <% if (error != null) { %>
        <div class="alert alert-error"><%= error %></div>
    <% } %>

    <section class="preus-list">
        <h2>Preus Actuals</h2>
        <% if (preus != null && !preus.isEmpty()) { %>
        <table>
            <thead>
                <tr>
                    <th>Proveïdor</th>
                    <th>Preu</th>
                    <th>Accions</th>
                </tr>
            </thead>
            <tbody>
                <% for (String[] p : preus) { %>
                <tr>
                    <td><%= p[1] %> (<%= p[0] %>)</td>
                    <td><%= p[2] %> €</td>
                    <td>
                        <form method="post" action="ComponentServlet" style="display:inline;">
                            <input type="hidden" name="action" value="deletePreu">
                            <input type="hidden" name="codi" value="<%= comp.getCodi() %>">
                            <input type="hidden" name="proveidor" value="<%= p[0] %>">
                            <button type="submit" class="btn-delete" onclick="return confirm('Eliminar aquest preu?')">
                                <span class="material-icons">delete</span>
                            </button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <% } else { %>
        <p class="no-data">No hi ha preus assignats per aquest component</p>
        <% } %>
    </section>

    <section class="add-preu">
        <h2>Afegir Preu</h2>
        <form method="post" action="ComponentServlet">
            <input type="hidden" name="action" value="addPreu">
            <input type="hidden" name="codi" value="<%= comp.getCodi() %>">
            
            <div class="form-row">
                <div class="form-group">
                    <label for="proveidor">Proveïdor</label>
                    <select id="proveidor" name="proveidor" required>
                        <option value="">-- Selecciona --</option>
                        <% if (proveidors != null) {
                            for (Proveidor prov : proveidors) { %>
                            <option value="<%= prov.getCodi() %>"><%= prov.getRaoSocial() %></option>
                        <% } } %>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="preu">Preu (€)</label>
                    <input type="number" id="preu" name="preu" min="1" required>
                </div>
                
                <button type="submit" class="btn-primary">
                    <span class="material-icons">add</span> Afegir
                </button>
            </div>
        </form>
    </section>
    <% } else { %>
    <p class="error">Component no trobat</p>
    <% } %>
</main>

<p id="author">Victor Villagra</p>

</body>
</html>
