<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="p1t4model.Producte"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vista en Arbre - AutoFactory</title>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/arbre.css">
</head>
<body>

<header>
    <a href="menu.jsp"><img src="img/logo_autofactory.png" alt="AutoFactory Logo"></a>
    <nav>
        <a href="menu.jsp">Home</a>
        <a href="ComponentServlet">Components</a>
        <a href="ProductServlet">Productes</a>
        <a href="LogoutServlet" class="logout">Logout</a>
    </nav>
</header>

<main>
    <h1>Vista en Arbre del Producte</h1>
    
    <form method="get" action="ArbreServlet" class="select-form">
        <select name="producte">
            <%
                List<Producte> productes = (List<Producte>) request.getAttribute("productes");
                String sel = (String) request.getAttribute("prodSeleccionat");
                if (productes != null)
                    for (Producte p : productes) {
            %>
            <option value="<%=p.getCodi()%>" <%=p.getCodi().equals(sel) ? "selected" : ""%>>
                <%=p.getNom()%>
            </option>
            <% } %>
        </select>
        <button type="submit"><span class="material-icons">account_tree</span> Mostrar</button>
    </form>
    
    <%
        List<String[]> bom = (List<String[]>) request.getAttribute("bom");
        if (bom != null && !bom.isEmpty()) {
    %>
    <div class="arbre-container">
        <ul class="arbre">
            <li>
                <span class="node producte"><span class="material-icons">inventory_2</span> Producte</span>
                <ul>
                    <% for (String[] l : bom) { %>
                    <li>
                        <span class="node <%= l[2].equals("s") ? "producte" : "component" %>">
                            <span class="material-icons"><%= l[2].equals("s") ? "inventory_2" : "settings" %></span>
                            <%= l[1] %> <span class="quantitat">(x<%= l[3] %>)</span>
                        </span>
                    </li>
                    <% } %>
                </ul>
            </li>
        </ul>
    </div>
    <% } else if (sel != null) { %>
    <p class="no-data">Aquest producte no té components assignats.</p>
    <% } %>
</main>

<p id="author">Victor Villagra</p>

</body>
</html>