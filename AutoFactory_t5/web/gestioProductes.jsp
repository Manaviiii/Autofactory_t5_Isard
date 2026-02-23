<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="p1t4model.Producte"%>
<%
    Integer paginaActual = (Integer) request.getAttribute("paginaActual");
    Integer totalPagines = (Integer) request.getAttribute("totalPagines");
    Integer totalProductes = (Integer) request.getAttribute("totalProductes");
    String filtreNom = (String) request.getAttribute("filtreNom");
    
    if (paginaActual == null) paginaActual = 1;
    if (totalPagines == null) totalPagines = 1;
    if (totalProductes == null) totalProductes = 0;
    
    String queryParams = "";
    if (filtreNom != null && !filtreNom.isEmpty()) {
        queryParams += "&filter=" + filtreNom;
    }
%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestió de Productes - AutoFactory</title>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/gestioProductes.css">
</head>
<body>

<header>
    <a href="menu.jsp"><img src="img/logo_autofactory.png" alt="AutoFactory Logo"></a>
    <nav>
        <a href="menu.jsp">Home</a>
        <a href="ComponentServlet">Components</a>
        <a href="ArbreServlet">Vista Arbre</a>
        <a href="LogoutServlet" class="logout">Logout</a>
    </nav>
</header>

<main>
    <section class="page-header">
        <h1>Gestió de Productes</h1>
        <div class="actions">
            <button onclick="location.href='ProductServlet?action=new'" class="btn-primary">
                <span class="material-icons">add</span> Nou producte
            </button>
            <button class="btn-danger" onclick="activarEliminar()">
                <span class="material-icons">delete</span> Eliminar
            </button>
            <button onclick="window.open('http://localhost:8080/jasperserver/rest_v2/reports/Reports/Blank_A4.pdf', '_blank')" class="btn-report">
                <span class="material-icons">picture_as_pdf</span> Informe PDF
            </button>
        </div>
    </section>

    <% String msg = request.getParameter("msg");
       String error = request.getParameter("error");
       if (msg != null) { %>
        <div class="alert alert-success"><span class="material-icons">check_circle</span> <%= msg %></div>
    <% } if (error != null) { %>
        <div class="alert alert-error"><span class="material-icons">error</span> <%= error %></div>
    <% } %>

    <section class="filters">
        <form method="get" action="ProductServlet">
            <input type="text" name="filter" placeholder="Filtrar per nom..." 
                   value="<%= filtreNom != null ? filtreNom : "" %>">
            <button type="submit" class="btn-filter"><span class="material-icons">search</span> Filtrar</button>
            <a href="ProductServlet" class="btn-clear">Netejar</a>
        </form>
    </section>

    <section class="llistat">
        <form method="post" action="ProductServlet" id="deleteForm">
            <table>
                <thead>
                    <tr>
                        <th class="checkCell"></th>
                        <th>Codi</th>
                        <th>Nom</th>
                        <th>Descripció</th>
                        <th>Stock</th>
                        <th>Accions</th>
                    </tr>
                </thead>
                <tbody>
                    <% List<Producte> productes = (List<Producte>) request.getAttribute("productes");
                       if (productes != null && !productes.isEmpty()) {
                           for (Producte p : productes) { %>
                    <tr>
                        <td class="checkCell"><input type="checkbox" name="deleteIds" value="<%= p.getCodi() %>" class="deleteCheck"></td>
                        <td><%= p.getCodi() %></td>
                        <td><%= p.getNom() %></td>
                        <td><%= p.getDescrip() != null ? p.getDescrip() : "-" %></td>
                        <td><%= p.getStock() %></td>
                        <td class="accions">
                            <span class="material-icons edit" onclick="location.href='ProductServlet?action=edit&id=<%= p.getCodi() %>'" title="Editar">edit</span>
                            <span class="material-icons bom" onclick="location.href='BomServlet?producte=<%= p.getCodi() %>'" title="Composició">account_tree</span>
                        </td>
                    </tr>
                    <% } } else { %>
                    <tr><td colspan="6" class="no-data">No hi ha productes per mostrar</td></tr>
                    <% } %>
                </tbody>
            </table>
            <div class="delete-actions">
                <button type="submit" name="action" value="deleteSelected" id="confirmDeleteBtn" class="btn-danger">
                    <span class="material-icons">delete_forever</span> Confirmar eliminació
                </button>
                <button type="button" id="cancelDeleteBtn" class="btn-secondary" onclick="cancelarEliminar()">Cancel·lar</button>
            </div>
        </form>
        
        <% if (totalPagines > 1) { %>
        <div class="pagination">
            <span class="pagination-info">Mostrant pàgina <%= paginaActual %> de <%= totalPagines %> (<%= totalProductes %> productes)</span>
            <div class="pagination-buttons">
                <% if (paginaActual > 1) { %>
                    <a href="ProductServlet?pagina=1<%= queryParams %>" class="pagination-btn" title="Primera">
                        <span class="material-icons">first_page</span>
                    </a>
                    <a href="ProductServlet?pagina=<%= paginaActual - 1 %><%= queryParams %>" class="pagination-btn" title="Anterior">
                        <span class="material-icons">chevron_left</span>
                    </a>
                <% } %>
                
                <% for (int i = 1; i <= totalPagines; i++) { %>
                    <a href="ProductServlet?pagina=<%= i %><%= queryParams %>" 
                       class="pagination-btn <%= i == paginaActual ? "active" : "" %>">
                        <%= i %>
                    </a>
                <% } %>
                
                <% if (paginaActual < totalPagines) { %>
                    <a href="ProductServlet?pagina=<%= paginaActual + 1 %><%= queryParams %>" class="pagination-btn" title="Següent">
                        <span class="material-icons">chevron_right</span>
                    </a>
                    <a href="ProductServlet?pagina=<%= totalPagines %><%= queryParams %>" class="pagination-btn" title="Última">
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
    if (checked.length === 0) { e.preventDefault(); alert('Selecciona almenys un producte'); }
    else if (!confirm('Eliminar ' + checked.length + ' producte/s?')) { e.preventDefault(); }
});
</script>
</body>
</html>