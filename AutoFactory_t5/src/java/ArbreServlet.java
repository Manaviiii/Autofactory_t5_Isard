import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import p1.t4.daooracle.DAOProducte;
import p1.t4.daooracle.DAOBom;
import p1t4model.Producte;

@WebServlet("/ArbreServlet")
public class ArbreServlet extends HttpServlet {

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

            if (codiProducte != null) {
                request.setAttribute("bom", daoBom.obtenirBom(codiProducte));
                request.setAttribute("prodSeleccionat", codiProducte);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        request.getRequestDispatcher("vistaArbre.jsp").forward(request, response);
    }
}
