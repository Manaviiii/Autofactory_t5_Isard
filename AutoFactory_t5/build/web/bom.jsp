<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="p1t4model.Producte"%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BOM - Composició de Productes - AutoFactory</title>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/bom.css">
</head>
<body>

<header>
    <a href="menu.jsp"><img src="img/logo_autofactory.png" alt="AutoFactory Logo"></a>
    <nav>
        <a href="menu.jsp">Home</a>
        <a href="ComponentServlet">Components</a>
        <a href="ProductServlet">Productes</a>
        <a href="ArbreServlet">Vista Arbre</a>
        <a href="LogoutServlet" class="logout">Logout</a>
    </nav>
</header>

<main>
    <section class="page-header">
        <h1>BOM · Bill of Materials</h1>
    </section>

    <% String msg = request.getParameter("msg");
       String error = request.getParameter("error");
       if (msg != null && !msg.isEmpty()) { %>
        <div class="alert alert-success"><span class="material-icons">check_circle</span> <%= msg %></div>
    <% } if (error != null && !error.isEmpty()) { %>
        <div class="alert alert-error"><span class="material-icons">error</span> <%= error %></div>
    <% } %>

    <section class="selector-producte">
        <form method="get" action="BomServlet">
            <label for="producte">Selecciona un producte:</label>
            <select name="producte" id="producte">
                <option value="">-- Selecciona --</option>
                <%
                    List<Producte> productes = (List<Producte>) request.getAttribute("productes");
                    String sel = (String) request.getAttribute("prodSeleccionat");
                    if (productes != null) {
                        for (Producte p : productes) {
                %>
                <option value="<%=p.getCodi()%>" <%=p.getCodi().equals(sel)?"selected":""%>>
                    <%=p.getNom()%> (<%=p.getCodi()%>)
                </option>
                <% }} %>
            </select>
            <button type="submit" class="btn-primary">
                <span class="material-icons">search</span> Carregar
            </button>
        </form>
    </section>

    <% if (sel != null && !sel.isEmpty()) { 
        String nomProducte = (String) request.getAttribute("nomProducte");
    %>
    
    <section class="bom-content">
        <h2>Composició de: <%= nomProducte %></h2>
        
        <%
            List<String[]> bom = (List<String[]>) request.getAttribute("bom");
            if (bom != null && !bom.isEmpty()) {
        %>
        <div class="llistat">
            <table>
                <thead>
                    <tr>
                        <th>Codi</th>
                        <th>Nom</th>
                        <th>Tipus</th>
                        <th>Quantitat</th>
                        <th>Accions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (String[] l : bom) { %>
                    <tr>
                        <td><%= l[0] %></td>
                        <td><%= l[1] %></td>
                        <td>
                            <span class="badge <%= l[2].equals("s") ? "badge-producte" : "badge-component" %>">
                                <%= l[2].equals("s") ? "Producte" : "Component" %>
                            </span>
                        </td>
                        <td>
                            <form method="post" action="BomServlet" class="form-quantitat">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="producte" value="<%= sel %>">
                                <input type="hidden" name="item" value="<%= l[0] %>">
                                <input type="number" name="quantitat" value="<%= l[3] %>" min="1" class="input-quantitat">
                                <button type="submit" class="btn-update" title="Actualitzar quantitat">
                                    <span class="material-icons">save</span>
                                </button>
                            </form>
                        </td>
                        <td class="accions">
                            <form method="post" action="BomServlet" class="form-delete" 
                                  onsubmit="return confirm('Segur que vols eliminar aquest element?')">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="producte" value="<%= sel %>">
                                <input type="hidden" name="item" value="<%= l[0] %>">
                                <button type="submit" class="btn-delete" title="Eliminar">
                                    <span class="material-icons">delete</span>
                                </button>
                            </form>
                            <% if (l[2].equals("s")) { %>
                            <a href="BomServlet?producte=<%= l[0] %>" class="btn-view" title="Veure composició">
                                <span class="material-icons">account_tree</span>
                            </a>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
        <p class="no-data">
            <span class="material-icons">info</span>
            Aquest producte no té components assignats.
        </p>
        <% } %>
        
        <div class="afegir-section">
            <h3><span class="material-icons">add_circle</span> Afegir element al BOM</h3>
            
            <%
                List<String[]> itemsDisponibles = (List<String[]>) request.getAttribute("itemsDisponibles");
                if (itemsDisponibles != null && !itemsDisponibles.isEmpty()) {
            %>
            <form method="post" action="BomServlet" class="form-afegir">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="producte" value="<%= sel %>">
                
                <div class="form-group">
                    <label for="item">Element:</label>
                    <select name="item" id="item" required>
                        <option value="">-- Selecciona un element --</option>
                        <optgroup label="Productes">
                            <% for (String[] item : itemsDisponibles) { 
                                if (item[2].equals("s")) { %>
                            <option value="<%= item[0] %>">🔧 <%= item[1] %> (<%= item[0] %>)</option>
                            <% }} %>
                        </optgroup>
                        <optgroup label="Components">
                            <% for (String[] item : itemsDisponibles) { 
                                if (!item[2].equals("s")) { %>
                            <option value="<%= item[0] %>">⚙️ <%= item[1] %> (<%= item[0] %>)</option>
                            <% }} %>
                        </optgroup>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="quantitat">Quantitat:</label>
                    <input type="number" name="quantitat" id="quantitat" min="1" value="1" required>
                </div>
                
                <button type="submit" class="btn-primary">
                    <span class="material-icons">add</span> Afegir al BOM
                </button>
            </form>
            <% } else { %>
            <p class="no-data">
                <span class="material-icons">info</span>
                No hi ha més elements disponibles per afegir.
            </p>
            <% } %>
        </div>
    </section>
    
    <% } else { %>
    <section class="info-inicial">
        <div class="info-box">
            <span class="material-icons">info</span>
            <p>Selecciona un producte per veure i gestionar la seva composició (BOM).</p>
        </div>
    </section>
    <% } %>
</main>

<p id="author">Victor Villagra</p>

</body>
</html>