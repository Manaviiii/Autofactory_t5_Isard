<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") != null) {
        response.sendRedirect("menu.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - AutoFactory</title>
    <link rel="stylesheet" href="css/login.css">
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<body>
    <main>
        <article class="logo-container">
            <img src="img/logo_autofactory.png" alt="AutoFactory Logo">
        </article>

        <h2>Iniciar sessió</h2>

        <%
            String error = request.getParameter("error");
            if (error != null && !error.isEmpty()) {
        %>
            <div class="error-message">
                <span class="material-icons">error</span>
                <span><%= error %></span>
            </div>
        <% } %>

        <form action="LoginServlet" method="post" id="loginForm">
            <div class="form-group">
                <label for="user">
                    <span class="material-icons">person</span>
                    Usuari
                </label>
                <input type="text" id="user" name="user" required placeholder="Introdueix el teu usuari">
            </div>

            <div class="form-group">
                <label for="pass">
                    <span class="material-icons">lock</span>
                    Contrasenya
                </label>
                <input type="password" id="pass" name="pass" required placeholder="Introdueix la teva contrasenya">
            </div>

            <button type="submit" class="btn-login">
                <span class="material-icons">login</span>
                Entrar
            </button>
        </form>
        
        <div class="info-box">
            <p><strong>Usuari:</strong></p>
            <p>admin / admin</p>
        </div>
    </main>
    
    <p id="author">Victor Villagra</p>
</body>
</html>
