<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="p1t4model.Component"%>
<%@page import="p1t4model.UnitatMesura"%>
<%
    Component comp = (Component) request.getAttribute("component");
    Boolean modeEdicio = (Boolean) request.getAttribute("modeEdicio");
    boolean editar = (modeEdicio != null && modeEdicio);
    List<UnitatMesura> mesures = (List<UnitatMesura>) request.getAttribute("mesures");
    List<String[]> preus = (List<String[]>) request.getAttribute("preus");
    List<String[]> proveidorsDisponibles = (List<String[]>) request.getAttribute("proveidorsDisponibles");
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
        <a href="ProveidorServlet">Proveïdors</a>
        <a href="ArbreServlet">Vista Arbre</a>
        <a href="LogoutServlet" class="logout">Logout</a>
    </nav>
</header>

<main>
    <h1><%= editar ? "Editar Component" : "Nou Component" %></h1>

    <% String msg = request.getParameter("msg");
       String error = request.getParameter("error");
       if (msg != null) { %>
        <div class="alert alert-success"><span class="material-icons">check_circle</span> <%= msg %></div>
    <% } if (error != null) { %>
        <div class="alert alert-error"><span class="material-icons">error</span> <%= error %></div>
    <% } %>

    <div class="form-container">
        <!-- FORMULARI DEL COMPONENT -->
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
                    <input type="number" id="preu" value="<%= comp != null ? comp.getPreuMig() : 0 %>" readonly class="readonly preu-mig">
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
                        <img src="ImageServlet?codi=<%= comp.getCodi() %>" id="previewImg">
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
                    Desar Component
                </button>
                <button type="button" class="btn-secondary" onclick="location.href='ComponentServlet'">
                    <span class="material-icons">cancel</span>
                    Cancel·lar
                </button>
            </div>
        </form>

        <% if (editar) { %>
        <!-- SEPARADOR -->
        <hr class="section-divider">

        <!-- SECCIÓ DE PREUS PER PROVEÏDOR -->
        <div class="preus-section">
            <h2><span class="material-icons">euro</span> Preus per Proveïdor</h2>
            <p class="section-info">El preu mig es calcula automàticament a partir dels preus dels proveïdors.</p>
            
            <% if (preus != null && !preus.isEmpty()) { %>
            <table class="preus-table">
                <thead>
                    <tr>
                        <th>Codi</th>
                        <th>Proveïdor</th>
                        <th>Preu (€)</th>
                        <th>Accions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (String[] p : preus) { %>
                    <tr>
                        <td><%= p[0] %></td>
                        <td><%= p[1] %></td>
                        <td>
                            <form method="post" action="CompresServlet" class="form-inline">
                                <input type="hidden" name="action" value="updatePreu">
                                <input type="hidden" name="component" value="<%= comp.getCodi() %>">
                                <input type="hidden" name="proveidor" value="<%= p[0] %>">
                                <input type="number" name="preu" value="<%= p[2] %>" min="0.01" step="0.01" class="input-preu">
                                <span class="euro">€</span>
                                <button type="submit" class="btn-icon btn-save" title="Guardar preu">
                                    <span class="material-icons">save</span>
                                </button>
                            </form>
                        </td>
                        <td>
                            <form method="post" action="CompresServlet" class="form-inline" onsubmit="return confirm('Eliminar aquest preu?')">
                                <input type="hidden" name="action" value="deletePreu">
                                <input type="hidden" name="component" value="<%= comp.getCodi() %>">
                                <input type="hidden" name="proveidor" value="<%= p[0] %>">
                                <button type="submit" class="btn-icon btn-delete" title="Eliminar">
                                    <span class="material-icons">delete</span>
                                </button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <% } else { %>
            <p class="no-preus"><span class="material-icons">info</span> No hi ha preus assignats per aquest component.</p>
            <% } %>

            <% if (proveidorsDisponibles != null && !proveidorsDisponibles.isEmpty()) { %>
            <div class="afegir-preu">
                <h3>Afegir nou proveïdor</h3>
                <form method="post" action="CompresServlet" class="form-afegir-preu">
                    <input type="hidden" name="action" value="addPreu">
                    <input type="hidden" name="component" value="<%= comp.getCodi() %>">
                    
                    <div class="form-group">
                        <label for="proveidor">Proveïdor</label>
                        <select name="proveidor" id="proveidor" required>
                            <option value="">-- Selecciona --</option>
                            <% for (String[] prov : proveidorsDisponibles) { %>
                            <option value="<%= prov[0] %>"><%= prov[1] %> (<%= prov[0] %>)</option>
                            <% } %>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="nouPreu">Preu (€)</label>
                        <input type="number" name="preu" id="nouPreu" min="0.01" step="0.01" placeholder="0.00" required>
                    </div>
                    
                    <button type="submit" class="btn-add">
                        <span class="material-icons">add</span> Afegir
                    </button>
                </form>
            </div>
            <% } else if (preus != null && !preus.isEmpty()) { %>
            <p class="no-preus"><span class="material-icons">check_circle</span> Tots els proveïdors ja tenen preu assignat.</p>
            <% } %>
        </div>
        <% } %>
    </div>
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
</script>

</body>
</html>