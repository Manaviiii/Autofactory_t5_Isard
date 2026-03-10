import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import p1.t4.daooracle.DAOProveidor;
import p1.t4.daooracle.DAOMunicipi;
import p1t4model.Proveidor;
import p1t4model.Municipi;

@WebServlet("/ProveidorServlet")
public class ProveidorServlet extends HttpServlet {
    
    private DAOProveidor daoProveidor;
    private DAOMunicipi daoMunicipi;

    @Override
    public void init() {
        daoProveidor = new DAOProveidor();
        daoMunicipi = new DAOMunicipi();
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
        String msg = null;
        String error = null;
        
        try {
            switch (action) {
                case "insert":
                    Proveidor nouProv = crearProveidorDesdeRequest(request);
                    if (daoProveidor.inserirProveidor(nouProv)) {
                        msg = "Proveïdor creat correctament";
                    } else {
                        error = "Error creant el proveïdor";
                    }
                    break;
                    
                case "update":
                    Proveidor provModificat = crearProveidorDesdeRequest(request);
                    if (daoProveidor.modificarProveidor(provModificat)) {
                        msg = "Proveïdor modificat correctament";
                    } else {
                        error = "Error modificant el proveïdor";
                    }
                    break;
                    
                case "delete":
                    String[] ids = request.getParameterValues("deleteIds");
                    if (ids != null && ids.length > 0) {
                        boolean totOk = true;
                        for (String id : ids) {
                            if (!daoProveidor.eliminarProveidor(id)) {
                                totOk = false;
                            }
                        }
                        if (totOk) {
                            msg = "Proveïdor/s eliminat/s correctament";
                        } else {
                            error = "Error eliminant algun proveïdor";
                        }
                    } else {
                        error = "No s'ha seleccionat cap proveïdor";
                    }
                    break;
                    
                default:
                    error = "Acció no reconeguda";
            }
        } catch (Exception e) {
            error = "Error: " + e.getMessage();
        }
        
        String redirect = "ProveidorServlet";
        if (msg != null) {
            redirect += "?msg=" + java.net.URLEncoder.encode(msg, "UTF-8");
        }
        if (error != null) {
            redirect += "?error=" + java.net.URLEncoder.encode(error, "UTF-8");
        }
        
        response.sendRedirect(redirect);
    }

    private void mostrarLlistat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Proveidor> proveidors = daoProveidor.llistarProveidors();
        request.setAttribute("proveidors", proveidors);
        request.getRequestDispatcher("gestioProveidors.jsp").forward(request, response);
    }

    private void prepararFormulari(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String codi = request.getParameter("codi");
        
        List<Municipi> municipis = daoMunicipi.llistarMunicipis();
        request.setAttribute("municipis", municipis);
        
        if (codi != null && !codi.isEmpty()) {
            Proveidor proveidor = daoProveidor.obtenirProveidorPerId(codi);
            if (proveidor != null) {
                request.setAttribute("proveidor", proveidor);
                request.setAttribute("modeEdicio", true);
            }
        }
        
        request.getRequestDispatcher("formProveidor.jsp").forward(request, response);
    }

    private Proveidor crearProveidorDesdeRequest(HttpServletRequest request) throws Exception {
        String codi = request.getParameter("codi");
        String cif = request.getParameter("cif");
        String raoSocial = request.getParameter("raoSocial");
        String adreca = request.getParameter("adreca");
        String municipi = request.getParameter("municipi");
        String personaContacte = request.getParameter("personaContacte");
        String telefon = request.getParameter("telefon");
        
        return new Proveidor(codi, cif, raoSocial, adreca, municipi, personaContacte, telefon);
    }
}