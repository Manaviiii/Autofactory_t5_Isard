import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import p1.t4.daooracle.DAOProducte;
import p1t4model.Producte;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    private DAOProducte dao;
    private static final int ITEMS_PER_PAGE = 5;

    @Override
    public void init() {
        dao = new DAOProducte();
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
                        resultat = "Producte creat correctament";
                    } else {
                        error = "Error creant el producte";
                    }
                    break;
                    
                case "update":
                    if (modificar(request)) {
                        resultat = "Producte modificat correctament";
                    } else {
                        error = "Error modificant el producte";
                    }
                    break;
                    
                case "deleteSelected":
                    if (eliminar(request)) {
                        resultat = "Producte/s eliminat/s correctament";
                    } else {
                        error = "Error eliminant el producte";
                    }
                    break;
                    
                default:
                    error = "Accio no reconeguda";
            }
        } catch (Exception e) {
            error = "Error: " + e.getMessage();
        }

        String redirect = "ProductServlet";
        if (resultat != null) {
            redirect += "?msg=" + java.net.URLEncoder.encode(resultat, "UTF-8");
        } else if (error != null) {
            redirect += "?error=" + java.net.URLEncoder.encode(error, "UTF-8");
        }
        response.sendRedirect(redirect);
    }

    private void mostrarLlistat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String filtreNom = request.getParameter("filter");
        
        List<Producte> productes = dao.llistarProductes();
        
        if (filtreNom != null && !filtreNom.trim().isEmpty()) {
            String filtre = filtreNom.toLowerCase().trim();
            productes = productes.stream()
                    .filter(p -> p.getNom().toLowerCase().contains(filtre))
                    .collect(Collectors.toList());
        }
        
        // Paginació
        int totalProductes = productes.size();
        int totalPagines = (int) Math.ceil((double) totalProductes / ITEMS_PER_PAGE);
        
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
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalProductes);
        
        List<Producte> productesPagina = productes.subList(startIndex, endIndex);
        
        request.setAttribute("productes", productesPagina);
        request.setAttribute("filtreNom", filtreNom);
        request.setAttribute("paginaActual", paginaActual);
        request.setAttribute("totalPagines", totalPagines);
        request.setAttribute("totalProductes", totalProductes);
        
        request.getRequestDispatcher("gestioProductes.jsp").forward(request, response);
    }

    private void prepararFormulari(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String id = request.getParameter("id");
        
        if (id != null && !id.isEmpty()) {
            Producte producte = dao.obtenirProductePerId(id);
            if (producte != null) {
                request.setAttribute("producte", producte);
                request.setAttribute("modeEdicio", true);
            }
        }
        
        request.getRequestDispatcher("formProducte.jsp").forward(request, response);
    }

    private boolean inserir(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            String nom = request.getParameter("nom");
            String descripcio = request.getParameter("descripcio");
            int stock = parseIntSafe(request.getParameter("stock"), 0);
            
            if (id == null || id.trim().isEmpty() ||
                nom == null || nom.trim().isEmpty()) {
                return false;
            }
            
            Producte p = new Producte(id.trim(), 's', nom.trim(), "dummy", stock, descripcio);
            return dao.inserirProducte(p);
            
        } catch (Exception e) {
            System.err.println("Error inserint producte: " + e.getMessage());
            return false;
        }
    }

    private boolean modificar(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            String nom = request.getParameter("nom");
            String descripcio = request.getParameter("descripcio");
            int stock = parseIntSafe(request.getParameter("stock"), 0);
            
            Producte p = new Producte(id.trim(), 's', nom.trim(), "dummy", stock, descripcio);
            return dao.modificarProducte(p);
            
        } catch (Exception e) {
            System.err.println("Error modificant producte: " + e.getMessage());
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
            if (!dao.eliminarProducte(id)) {
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