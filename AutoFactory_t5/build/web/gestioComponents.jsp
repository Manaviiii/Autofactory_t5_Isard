<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="p1t4model.Component"%>
<%
    Integer paginaActual = (Integer) request.getAttribute("paginaActual");
    Integer totalPagines = (Integer) request.getAttribute("totalPagines");
    Integer totalComponents = (Integer) request.getAttribute("totalComponents");
    String filtreNom = (String) request.getAttribute("filtreNom");
    String filtreFabricant = (String) request.getAttribute("filtreFabricant");
    
    if (paginaActual == null) paginaActual = 1;
    if (totalPagines == null) totalPagines = 1;
    if (totalComponents == null) totalComponents = 0;
    
    String queryParams = "";
    if (filtreNom != null && !filtreNom.isEmpty()) {
        queryParams += "&filtreNom=" + filtreNom;
    }
    if (filtreFabricant != null && !filtreFabricant.isEmpty()) {
        queryParams += "&filtreFabricant=" + filtreFabricant;
    }
%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestió de Components - AutoFactory</title>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/gestioComponents.css">
</head>
<body>

<header>
    <a href="menu.jsp"><img src="img/logo_autofactory.png" alt="AutoFactory Logo"></a>
    <nav>
        <a href="menu.jsp">Home</a>
        <a href="ProductServlet">Productes</a>
        <a href="ArbreServlet">Vista Arbre</a>
        <a href="LogoutServlet" class="logout">Logout</a>
    </nav>
</header>

<main>
    <section class="page-header">
        <h1>Gestió de Components</h1>
        <div class="actions">
            <button onclick="location.href='ComponentServlet?action=new'" class="btn-primary">
                <span class="material-icons">add</span> Nou component
            </button>
            <button class="btn-danger" onclick="activarEliminar()">
                <span class="material-icons">delete</span> Eliminar
            </button>
        </div>
    </section>

    <% String msg = request.getParameter("msg");
       String error = request.getParameter("error");
       if (msg != null && !msg.isEmpty()) { %>
        <div class="alert alert-success"><span class="material-icons">check_circle</span> <%= msg %></div>
    <% } if (error != null && !error.isEmpty()) { %>
        <div class="alert alert-error"><span class="material-icons">error</span> <%= error %></div>
    <% } %>

    <section class="filters">
        <form method="get" action="ComponentServlet">
            <input type="text" name="filtreNom" placeholder="Filtrar per nom..."
                   value="<%= filtreNom != null ? filtreNom : "" %>">
            <input type="text" name="filtreFabricant" placeholder="Filtrar per fabricant..."
                   value="<%= filtreFabricant != null ? filtreFabricant : "" %>">
            <button type="submit" class="btn-filter"><span class="material-icons">search</span> Filtrar</button>
            <a href="ComponentServlet" class="btn-clear">Netejar</a>
        </form>
    </section>

    <section class="llistat">
        <form method="post" action="ComponentServlet" id="deleteForm">
            <input type="hidden" name="action" value="delete">
            <table>
                <thead>
                    <tr>
                        <th class="checkCell"></th>
                        <th>Foto</th>
                        <th>Codi</th>
                        <th>Nom</th>
                        <th>Fabricant</th>
                        <th>Stock</th>
                        <th>Preu Mig</th>
                        <th>Accions</th>
                    </tr>
                </thead>
                <tbody>
                    <% List<Component> components = (List<Component>) request.getAttribute("components");
                       if (components != null && !components.isEmpty()) {
                           for (Component c : components) { %>
                    <tr>
                        <td class="checkCell"><input type="checkbox" name="deleteIds" value="<%= c.getCodi() %>" class="deleteCheck"></td>
                        <td class="fotoCell">
                            <img src="ImageServlet?codi=<%= c.getCodi() %>" alt="Foto" class="foto-mini" onerror="this.style.visibility='hidden'">
                        </td>
                        <td><%= c.getCodi() %></td>
                        <td><%= c.getNom() %></td>
                        <td><%= c.getCodiFabricant() %></td>
                        <td><%= c.getStock() %></td>
                        <td><%= c.getPreuMig() %> €</td>
                        <td class="accions">
                            <span class="material-icons edit" onclick="location.href='ComponentServlet?action=edit&codi=<%= c.getCodi() %>'" title="Editar">edit</span>
                        </td>
                    </tr>
                    <% } } else { %>
                    <tr><td colspan="8" class="no-data">No hi ha components per mostrar</td></tr>
                    <% } %>
                </tbody>
            </table>
            <div class="delete-actions">
                <button type="submit" id="confirmDeleteBtn" class="btn-danger">
                    <span class="material-icons">delete_forever</span> Confirmar eliminació
                </button>
                <button type="button" id="cancelDeleteBtn" class="btn-secondary" onclick="cancelarEliminar()">Cancel·lar</button>
            </div>
        </form>
        
        <% if (totalPagines > 1) { %>
        <div class="pagination">
            <span class="pagination-info">Mostrant pàgina <%= paginaActual %> de <%= totalPagines %> (<%= totalComponents %> components)</span>
            <div class="pagination-buttons">
                <% if (paginaActual > 1) { %>
                    <a href="ComponentServlet?pagina=1<%= queryParams %>" class="pagination-btn" title="Primera">
                        <span class="material-icons">first_page</span>
                    </a>
                    <a href="ComponentServlet?pagina=<%= paginaActual - 1 %><%= queryParams %>" class="pagination-btn" title="Anterior">
                        <span class="material-icons">chevron_left</span>
                    </a>
                <% } %>
                
                <% for (int i = 1; i <= totalPagines; i++) { %>
                    <a href="ComponentServlet?pagina=<%= i %><%= queryParams %>" 
                       class="pagination-btn <%= i == paginaActual ? "active" : "" %>">
                        <%= i %>
                    </a>
                <% } %>
                
                <% if (paginaActual < totalPagines) { %>
                    <a href="ComponentServlet?pagina=<%= paginaActual + 1 %><%= queryParams %>" class="pagination-btn" title="Següent">
                        <span class="material-icons">chevron_right</span>
                    </a>
                    <a href="ComponentServlet?pagina=<%= totalPagines %><%= queryParams %>" class="pagination-btn" title="Última">
                        <span class="material-icons">last_page</span>
                    </a>
                <% } %>
            </div>
        </div>
        <% } %>
    </section>
</main>

<p id="author">Victor Villagra</p>

<script>
function activarEliminar() {
    document.querySelectorAll('.deleteCheck').forEach(ch => ch.style.display = 'block');
    document.getElementById('confirmDeleteBtn').style.display = 'inline-flex';
    document.getElementById('cancelDeleteBtn').style.display = 'inline-flex';
}
function cancelarEliminar() {
    document.querySelectorAll('.deleteCheck').forEach(ch => { ch.checked = false; ch.style.display = 'none'; });
    document.getElementById('confirmDeleteBtn').style.display = 'none';
    document.getElementById('cancelDeleteBtn').style.display = 'none';
}
document.getElementById('deleteForm').addEventListener('submit', function(e) {
    const checked = document.querySelectorAll('.deleteCheck:checked');
    if (checked.length === 0) { e.preventDefault(); alert('Selecciona almenys un component per eliminar'); }
    else if (!confirm('Segur que vols eliminar ' + checked.length + ' component/s?')) { e.preventDefault(); }
});
</script>
</body>
</html>