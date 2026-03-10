<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="p1t4model.Proveidor"%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestió de Proveïdors - AutoFactory</title>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/gestioProveidors.css">
</head>
<body>

<header>
    <a href="menu.jsp"><img src="img/logo_autofactory.png" alt="AutoFactory Logo"></a>
    <nav>
        <a href="menu.jsp">Home</a>
        <a href="ComponentServlet">Components</a>
        <a href="ProductServlet">Productes</a>
        <a href="ProveidorServlet" class="active">Proveïdors</a>
        <a href="ArbreServlet">Vista Arbre</a>
        <a href="LogoutServlet" class="logout">Logout</a>
    </nav>
</header>

<main>
    <section class="page-header">
        <h1>Gestió de Proveïdors</h1>
        <div class="actions">
            <button onclick="location.href='ProveidorServlet?action=new'" class="btn-primary">
                <span class="material-icons">add</span> Nou Proveïdor
            </button>
            <button class="btn-danger" onclick="activarEliminar()">
                <span class="material-icons">delete</span> Eliminar
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

    <section class="llistat">
        <form method="post" action="ProveidorServlet" id="deleteForm">
            <input type="hidden" name="action" value="delete">
            <table>
                <thead>
                    <tr>
                        <th class="checkCell"></th>
                        <th>Codi</th>
                        <th>CIF</th>
                        <th>Raó Social</th>
                        <th>Persona Contacte</th>
                        <th>Telèfon</th>
                        <th>Accions</th>
                    </tr>
                </thead>
                <tbody>
                    <% List<Proveidor> proveidors = (List<Proveidor>) request.getAttribute("proveidors");
                       if (proveidors != null && !proveidors.isEmpty()) {
                           for (Proveidor p : proveidors) { %>
                    <tr>
                        <td class="checkCell"><input type="checkbox" name="deleteIds" value="<%= p.getCodi() %>" class="deleteCheck"></td>
                        <td><%= p.getCodi() %></td>
                        <td><%= p.getCIF() %></td>
                        <td><%= p.getRaoSocial() %></td>
                        <td><%= p.getPersonaContacte() != null ? p.getPersonaContacte() : "-" %></td>
                        <td><%= p.getTelfContacte() != null ? p.getTelfContacte() : "-" %></td>
                        <td class="accions">
                            <span class="material-icons edit" onclick="location.href='ProveidorServlet?action=edit&codi=<%= p.getCodi() %>'" title="Editar">edit</span>
                        </td>
                    </tr>
                    <% } } else { %>
                    <tr><td colspan="7" class="no-data">No hi ha proveïdors per mostrar</td></tr>
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
    if (checked.length === 0) { e.preventDefault(); alert('Selecciona almenys un proveïdor'); }
    else if (!confirm('Eliminar ' + checked.length + ' proveïdor/s?')) { e.preventDefault(); }
});
</script>
</body>
</html>