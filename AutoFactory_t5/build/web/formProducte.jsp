<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="p1t4model.Producte"%>
<%
    Producte prod = (Producte) request.getAttribute("producte");
    Boolean modeEdicio = (Boolean) request.getAttribute("modeEdicio");
    boolean editar = (modeEdicio != null && modeEdicio);
%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= editar ? "Editar Producte" : "Nou Producte" %> - AutoFactory</title>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/formProducte.css">
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
    <h1><%= editar ? "Editar Producte" : "Nou Producte" %></h1>
    
    <form method="post" action="ProductServlet" id="producteForm">
        <input type="hidden" name="action" value="<%= editar ? "update" : "insert" %>">
        
        <div class="form-group">
            <label for="id">Codi *</label>
            <% if (editar) { %>
                <input type="text" id="id" value="<%= prod.getCodi() %>" readonly class="readonly">
                <input type="hidden" name="id" value="<%= prod.getCodi() %>">
            <% } else { %>
                <input type="text" id="id" name="id" required maxlength="10" placeholder="Màxim 10 caràcters">
            <% } %>
        </div>
        
        <div class="form-group">
            <label for="nom">Nom *</label>
            <input type="text" id="nom" name="nom" required maxlength="20" 
                   value="<%= prod != null ? prod.getNom() : "" %>" placeholder="Màxim 20 caràcters">
        </div>
        
        <div class="form-group">
            <label for="descripcio">Descripció</label>
            <textarea id="descripcio" name="descripcio" rows="3" maxlength="100"
                      placeholder="Màxim 100 caràcters"><%= prod != null && prod.getDescrip() != null ? prod.getDescrip() : "" %></textarea>
        </div>
        
        <div class="form-group">
            <label for="stock">Stock</label>
            <input type="number" id="stock" name="stock" min="0" value="<%= prod != null ? prod.getStock() : 0 %>">
        </div>
        
        <div class="form-actions">
            <button type="submit" class="btn-primary">
                <span class="material-icons">save</span> Desar
            </button>
            <button type="button" class="btn-secondary" onclick="location.href='ProductServlet'">
                <span class="material-icons">cancel</span> Cancel·lar
            </button>
        </div>
    </form>
</main>

<p id="author">Victor Villagra</p>

<script>
document.getElementById('producteForm').addEventListener('submit', function(e) {
    const id = document.getElementById('id').value.trim();
    const nom = document.getElementById('nom').value.trim();
    if (id.length === 0) { e.preventDefault(); alert('El codi és obligatori'); return; }
    if (nom.length === 0) { e.preventDefault(); alert('El nom és obligatori'); return; }
});
</script>
</body>
</html>