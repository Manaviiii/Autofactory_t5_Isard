import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import p1.t4.daooracle.DAOCompres;

@WebServlet("/CompresServlet")
public class CompresServlet extends HttpServlet {
    
    private DAOCompres daoCompres;

    @Override
    public void init() {
        daoCompres = new DAOCompres();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String codiComponent = request.getParameter("component");
        String msg = null;
        String error = null;
        
        try {
            switch (action) {
                case "addPreu":
                    String codiProv = request.getParameter("proveidor");
                    double preu = Double.parseDouble(request.getParameter("preu"));
                    
                    if (preu <= 0) {
                        error = "El preu ha de ser major que 0";
                    } else if (daoCompres.existeix(codiComponent, codiProv)) {
                        error = "Aquest proveïdor ja té un preu assignat";
                    } else if (daoCompres.afegirPreu(codiComponent, codiProv, preu)) {
                        msg = "Preu afegit correctament";
                    } else {
                        error = "Error afegint preu";
                    }
                    break;
                    
                case "updatePreu":
                    String provModificar = request.getParameter("proveidor");
                    double nouPreu = Double.parseDouble(request.getParameter("preu"));
                    
                    if (nouPreu <= 0) {
                        error = "El preu ha de ser major que 0";
                    } else if (daoCompres.modificarPreu(codiComponent, provModificar, nouPreu)) {
                        msg = "Preu modificat correctament";
                    } else {
                        error = "Error modificant preu";
                    }
                    break;
                    
                case "deletePreu":
                    String provEliminar = request.getParameter("proveidor");
                    if (daoCompres.eliminarPreu(codiComponent, provEliminar)) {
                        msg = "Preu eliminat correctament";
                    } else {
                        error = "Error eliminant preu";
                    }
                    break;
                    
                default:
                    error = "Acció no reconeguda";
            }
        } catch (NumberFormatException e) {
            error = "El preu ha de ser un número vàlid";
        } catch (Exception e) {
            error = "Error: " + e.getMessage();
        }
        
        String redirect = "ComponentServlet?action=edit&codi=" + codiComponent;
        if (msg != null) {
            redirect += "&msg=" + java.net.URLEncoder.encode(msg, "UTF-8");
        }
        if (error != null) {
            redirect += "&error=" + java.net.URLEncoder.encode(error, "UTF-8");
        }
        
        response.sendRedirect(redirect);
    }
}