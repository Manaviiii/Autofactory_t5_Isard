<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="p1t4model.Component"%>
<%@page import="p1t4model.UnitatMesura"%>
<%
    Component comp = (Component) request.getAttribute("component");
    Boolean modeEdicio = (Boolean) request.getAttribute("modeEdicio");
    boolean editar = (modeEdicio != null && modeEdicio);
    List<UnitatMesura> mesures = (List<UnitatMesura>) request.getAttribute("mesures");
%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= editar ? "Editar Component" : "Nou Component" %> - AutoFactory</title>
    <link href="https://fonts.googleapis.com/css2?family=Kaushan+Script&family=Playfair+Display:wght@700&family=Lora:ital,wght@1,600&family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/formComponent.css">
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
    <h1><%= editar ? "Editar Component" : "Nou Component" %></h1>

    <form method="post" action="ComponentServlet" id="componentForm" enctype="multipart/form-data">
        <input type="hidden" name="action" value="<%= editar ? "update" : "insert" %>">
        
        <div class="form-group">
            <label for="codi">Codi *</label>
            <% if (editar) { %>
                <input type="text" id="codi" value="<%= comp.getCodi() %>" readonly class="readonly">
                <input type="hidden" name="codi" value="<%= comp.getCodi() %>">
            <% } else { %>
                <input type="text" id="codi" name="codi" required maxlength="10" placeholder="Màxim 10 caràcters">
            <% } %>
        </div>

        <div class="form-group">
            <label for="nom">Nom *</label>
            <input type="text" id="nom" name="nom" required maxlength="20" 
                   value="<%= comp != null ? comp.getNom() : "" %>" placeholder="Màxim 20 caràcters">
        </div>

        <div class="form-group">
            <label for="descrip">Descripció</label>
            <textarea id="descrip" name="descrip" rows="3" maxlength="100" 
                      placeholder="Màxim 100 caràcters"><%= comp != null && comp.getDescrip() != null ? comp.getDescrip() : "" %></textarea>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="stock">Stock</label>
                <input type="number" id="stock" name="stock" min="0" 
                       value="<%= comp != null ? comp.getStock() : 0 %>">
            </div>

            <div class="form-group">
                <label for="preu">Preu Mig (€)</label>
                <input type="number" id="preu" value="<%= comp != null ? comp.getPreuMig() : 0 %>" readonly class="readonly">
            </div>
        </div>

        <div class="form-group">
            <label for="fabricant">Codi Fabricant *</label>
            <input type="text" id="fabricant" name="fabricant" required maxlength="10" 
                   value="<%= comp != null ? comp.getCodiFabricant() : "" %>" placeholder="Màxim 10 caràcters">
        </div>

        <div class="form-group">
            <label for="unitat">Unitat de Mesura *</label>
            <% if (mesures != null && !mesures.isEmpty()) { %>
                <select id="unitat" name="unitat" required>
                    <option value="">-- Selecciona --</option>
                    <% for (UnitatMesura m : mesures) { %>
                        <option value="<%= m.getCodiMesura() %>" 
                            <%= comp != null && comp.getUnitatMesura().equals(m.getCodiMesura()) ? "selected" : "" %>>
                            <%= m.getNomUnitat() %> (<%= m.getCodiMesura() %>)
                        </option>
                    <% } %>
                </select>
            <% } else { %>
                <input type="text" id="unitat" name="unitat" required maxlength="5" 
                       value="<%= comp != null ? comp.getUnitatMesura() : "" %>" placeholder="Màxim 5 caràcters">
            <% } %>
        </div>

        <div class="form-group">
            <label for="foto">Foto del Component</label>
            <% if (editar) { %>
                <div class="foto-preview">
                    <img src="ImageServlet?codi=<%= comp.getCodi() %>" id="previewImg"">
                </div>
            <% } %>
            <input type="file" id="foto" name="foto" accept="image/*" onchange="previewImage(this)">
            <small>Formats acceptats: JPG, PNG, GIF. Màxim 5MB.</small>
            <% if (!editar) { %>
                <div class="foto-preview" style="display:none;">
                    <img src="" alt="Preview" id="previewImg">
                </div>
            <% } %>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn-primary">
                <span class="material-icons">save</span>
                Desar
            </button>
            <button type="button" class="btn-secondary" onclick="location.href='ComponentServlet'">
                <span class="material-icons">cancel</span>
                Cancel·lar
            </button>
        </div>
    </form>
</main>

<p id="author">Victor Villagra</p>

<script>
    function previewImage(input) {
        const preview = document.getElementById('previewImg');
        const previewContainer = preview.parentElement;
        
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                preview.src = e.target.result;
                previewContainer.style.display = 'block';
            }
            reader.readAsDataURL(input.files[0]);
        }
    }

    document.getElementById('componentForm').addEventListener('submit', function(e) {
        const codi = document.getElementById('codi').value.trim();
        const nom = document.getElementById('nom').value.trim();
        const fabricant = document.getElementById('fabricant').value.trim();
        
        if (codi.length === 0) {
            e.preventDefault();
            alert('El codi és obligatori');
            return;
        }
        
        if (nom.length === 0) {
            e.preventDefault();
            alert('El nom és obligatori');
            return;
        }
        
        if (fabricant.length === 0) {
            e.preventDefault();
            alert('El codi de fabricant és obligatori');
            return;
        }
    });
</script>

</body>
</html>