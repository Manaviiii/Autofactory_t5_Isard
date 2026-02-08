import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import p1.t4.daooracle.DAOComponent;
import p1.t4.daooracle.DAOMesura;
import p1t4model.Component;
import p1t4model.UnitatMesura;

@WebServlet("/ComponentServlet")
@MultipartConfig(maxFileSize = 5242880) // 5MB max
public class ComponentServlet extends HttpServlet {

    private DAOComponent daoComponent;
    private DAOMesura daoMesura;
    private static final int ITEMS_PER_PAGE = 5;

    @Override
    public void init() throws ServletException {
        daoComponent = new DAOComponent();
        daoMesura = new DAOMesura();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        
        if ("edit".equals(action) || "new".equals(action)) {
            prepararFormulari(request, response);
        } else {
            mostrarLlistat(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String resultat = null;
        String error = null;

        try {
            switch (action) {
                case "insert":
                    if (inserir(request)) {
                        resultat = "Component creat correctament";
                    } else {
                        error = "Error creant el component";
                    }
                    break;
                    
                case "update":
                    if (modificar(request)) {
                        resultat = "Component modificat correctament";
                    } else {
                        error = "Error modificant el component";
                    }
                    break;
                    
                case "delete":
                    if (eliminar(request)) {
                        resultat = "Component/s eliminat/s correctament";
                    } else {
                        error = "Error eliminant el component";
                    }
                    break;
                    
                default:
                    error = "Accio no reconeguda";
            }
        } catch (Exception e) {
            error = "Error: " + e.getMessage();
        }

        String redirect = "ComponentServlet";
        if (resultat != null) {
            redirect += "?msg=" + java.net.URLEncoder.encode(resultat, "UTF-8");
        } else if (error != null) {
            redirect += "?error=" + java.net.URLEncoder.encode(error, "UTF-8");
        }
        response.sendRedirect(redirect);
    }

    private void mostrarLlistat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String filtreNom = request.getParameter("filtreNom");
        String filtreFabricant = request.getParameter("filtreFabricant");
        
        List<Component> components = daoComponent.listarTodos();
        
        if (filtreNom != null && !filtreNom.trim().isEmpty()) {
            String filtre = filtreNom.toLowerCase().trim();
            components = components.stream()
                    .filter(c -> c.getNom().toLowerCase().contains(filtre))
                    .collect(Collectors.toList());
        }
        
        if (filtreFabricant != null && !filtreFabricant.trim().isEmpty()) {
            String filtre = filtreFabricant.toLowerCase().trim();
            components = components.stream()
                    .filter(c -> c.getCodiFabricant().toLowerCase().contains(filtre))
                    .collect(Collectors.toList());
        }
        
        // Paginació
        int totalComponents = components.size();
        int totalPagines = (int) Math.ceil((double) totalComponents / ITEMS_PER_PAGE);
        
        int paginaActual = 1;
        String paginaParam = request.getParameter("pagina");
        if (paginaParam != null) {
            try {
                paginaActual = Integer.parseInt(paginaParam);
                if (paginaActual < 1) paginaActual = 1;
                if (paginaActual > totalPagines && totalPagines > 0) paginaActual = totalPagines;
            } catch (NumberFormatException e) {
                paginaActual = 1;
            }
        }
        
        int startIndex = (paginaActual - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalComponents);
        
        List<Component> componentsPagina = components.subList(startIndex, endIndex);
        
        request.setAttribute("components", componentsPagina);
        request.setAttribute("filtreNom", filtreNom);
        request.setAttribute("filtreFabricant", filtreFabricant);
        request.setAttribute("paginaActual", paginaActual);
        request.setAttribute("totalPagines", totalPagines);
        request.setAttribute("totalComponents", totalComponents);
        
        request.getRequestDispatcher("gestioComponents.jsp").forward(request, response);
    }

    private void prepararFormulari(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String codi = request.getParameter("codi");
        
        List<UnitatMesura> mesures = daoMesura.llistarMesures();
        request.setAttribute("mesures", mesures);
        
        if (codi != null && !codi.isEmpty()) {
            Component component = daoComponent.buscarPorCodigo(codi);
            if (component != null) {
                request.setAttribute("component", component);
                request.setAttribute("modeEdicio", true);
            }
        }
        
        request.getRequestDispatcher("formComponent.jsp").forward(request, response);
    }

    private boolean inserir(HttpServletRequest request) {
        try {
            String codi = request.getParameter("codi");
            String nom = request.getParameter("nom");
            String descrip = request.getParameter("descrip");
            int stock = parseIntSafe(request.getParameter("stock"), 0);
            String fabricant = request.getParameter("fabricant");
            int preu = parseIntSafe(request.getParameter("preu"), 0);
            String unitat = request.getParameter("unitat");
            
            if (codi == null || codi.trim().isEmpty() ||
                nom == null || nom.trim().isEmpty() ||
                fabricant == null || fabricant.trim().isEmpty() ||
                unitat == null || unitat.trim().isEmpty()) {
                return false;
            }
            
            Component c = new Component(codi.trim(), 'n', nom.trim(), "dummy", 
                    stock, descrip, fabricant.trim(), unitat.trim());
            c.setPreuMig(preu);
            
            boolean creat = daoComponent.crear(c);
            
            // Guardar foto si s'ha pujat
            if (creat) {
                Part filePart = request.getPart("foto");
                if (filePart != null && filePart.getSize() > 0) {
                    InputStream is = filePart.getInputStream();
                    byte[] fotoBytes = is.readAllBytes();
                    daoComponent.actualizarFoto(codi.trim(), fotoBytes);
                }
            }
            
            return creat;
            
        } catch (Exception e) {
            System.err.println("Error inserint component: " + e.getMessage());
            return false;
        }
    }

    private boolean modificar(HttpServletRequest request) {
        try {
            String codi = request.getParameter("codi");
            String nom = request.getParameter("nom");
            String descrip = request.getParameter("descrip");
            int stock = parseIntSafe(request.getParameter("stock"), 0);
            String fabricant = request.getParameter("fabricant");
            int preu = parseIntSafe(request.getParameter("preu"), 0);
            String unitat = request.getParameter("unitat");
            
            Component c = new Component(codi.trim(), 'n', nom.trim(), "dummy", 
                    stock, descrip, fabricant.trim(), unitat.trim());
            c.setPreuMig(preu);
            
            boolean actualitzat = daoComponent.actualizar(c);
            
            // Actualitzar foto si s'ha pujat una nova
            if (actualitzat) {
                Part filePart = request.getPart("foto");
                if (filePart != null && filePart.getSize() > 0) {
                    InputStream is = filePart.getInputStream();
                    byte[] fotoBytes = is.readAllBytes();
                    daoComponent.actualizarFoto(codi.trim(), fotoBytes);
                }
            }
            
            return actualitzat;
            
        } catch (Exception e) {
            System.err.println("Error modificant component: " + e.getMessage());
            return false;
        }
    }

    private boolean eliminar(HttpServletRequest request) {
        String[] ids = request.getParameterValues("deleteIds");
        
        if (ids == null || ids.length == 0) {
            return false;
        }
        
        boolean totOk = true;
        for (String id : ids) {
            if (!daoComponent.eliminar(id)) {
                totOk = false;
            }
        }
        
        return totOk;
    }

    private int parseIntSafe(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}