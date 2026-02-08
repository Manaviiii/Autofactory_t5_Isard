<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String user = (String) session.getAttribute("user");
    String nomComplet = (String) session.getAttribute("nomComplet");
%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Principal - AutoFactory</title>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Lora:ital,wght@1,600&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/menu.css">
</head>
<body>

<header>
    <a href="menu.jsp">
        <img src="img/logo_autofactory.png" alt="AutoFactory Logo">
    </a>
    <div class="user-info">
        <span class="material-icons">account_circle</span>
        <span><%= nomComplet != null ? nomComplet : user %></span>
        <a href="LogoutServlet" title="Tancar sessió">
            <span class="material-icons">logout</span>
        </a>
    </div>
</header>

<main>
    <h1>Menú Principal</h1>
    <p class="welcome">Benvingut, Víctor!</p>

    <div class="menu-grid">
        <div class="menu-card" onclick="location.href='ProductServlet'">
            <span class="material-icons">inventory_2</span>
            <h3>Gestió de Productes</h3>
            <p>Crear, modificar i eliminar productes</p>
        </div>
        
        <div class="menu-card" onclick="location.href='ComponentServlet'">
            <span class="material-icons">settings</span>
            <h3>Gestió de Components</h3>
            <p>Administrar components i preus</p>
        </div>
        
        <div class="menu-card" onclick="location.href='BomServlet'">
            <span class="material-icons">account_tree</span>
            <h3>Composició (BOM)</h3>
            <p>Gestionar composició de productes</p>
        </div>
        
        <div class="menu-card" onclick="location.href='ArbreServlet'">
            <span class="material-icons">park</span>
            <h3>Vista en Arbre</h3>
            <p>Visualitzar estructura de productes</p>
        </div>
    </div>
</main>

<p id="author">Victor Villagra</p>

</body>
</html>
