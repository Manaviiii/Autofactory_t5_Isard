import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import p1.t4.daooracle.DAOProducte;
import p1.t4.daooracle.DAOBom;
import p1t4model.Producte;

@WebServlet("/BomServlet")
public class BomServlet extends HttpServlet {
    
    private DAOProducte daoProducte;
    private DAOBom daoBom;

    @Override
    public void init() {
        daoProducte = new DAOProducte();
        daoBom = new DAOBom();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String codiProducte = request.getParameter("producte");
        
        try {
            List<Producte> productes = daoProducte.llistarProductes();
            request.setAttribute("productes", productes);
            
            if (codiProducte != null && !codiProducte.isEmpty()) {
                request.setAttribute("bom", daoBom.obtenirBom(codiProducte));
                request.setAttribute("itemsDisponibles", daoBom.obtenirItemsDisponibles(codiProducte));
                request.setAttribute("prodSeleccionat", codiProducte);
                
                for (Producte p : productes) {
                    if (p.getCodi().equals(codiProducte)) {
                        request.setAttribute("nomProducte", p.getNom());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error BomServlet doGet: " + e.getMessage());
            request.setAttribute("error", "Error carregant dades: " + e.getMessage());
        }
        
        request.getRequestDispatcher("bom.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String codiProducte = request.getParameter("producte");
        String msg = null;
        String error = null;
        
        try {
            switch (action) {
                case "add":
                    String codiItem = request.getParameter("item");
                    int quantitat = Integer.parseInt(request.getParameter("quantitat"));
                    
                    if (codiProducte.equals(codiItem)) {
                        error = "No es pot afegir un producte a si mateix";
                    } else if (daoBom.existeix(codiProducte, codiItem)) {
                        error = "Aquest element ja existeix al BOM";
                    } else if (daoBom.afegir(codiProducte, codiItem, quantitat)) {
                        msg = "Element afegit correctament";
                    } else {
                        error = "Error afegint element";
                    }
                    break;
                    
                case "delete":
                    String itemEliminar = request.getParameter("item");
                    if (daoBom.eliminar(codiProducte, itemEliminar)) {
                        msg = "Element eliminat correctament";
                    } else {
                        error = "Error eliminant element";
                    }
                    break;
                    
                case "update":
                    String itemModificar = request.getParameter("item");
                    int novaQuantitat = Integer.parseInt(request.getParameter("quantitat"));
                    if (novaQuantitat <= 0) {
                        error = "La quantitat ha de ser major que 0";
                    } else if (daoBom.modificarQuantitat(codiProducte, itemModificar, novaQuantitat)) {
                        msg = "Quantitat modificada correctament";
                    } else {
                        error = "Error modificant quantitat";
                    }
                    break;
                    
                default:
                    error = "Acció no reconeguda";
            }
        } catch (NumberFormatException e) {
            error = "La quantitat ha de ser un número vàlid";
        } catch (Exception e) {
            error = "Error: " + e.getMessage();
        }
        
        String redirect = "BomServlet?producte=" + codiProducte;
        if (msg != null) {
            redirect += "&msg=" + java.net.URLEncoder.encode(msg, "UTF-8");
        }
        if (error != null) {
            redirect += "&error=" + java.net.URLEncoder.encode(error, "UTF-8");
        }
        
        response.sendRedirect(redirect);
    }
}