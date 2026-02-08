import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.DAOUsuari;
import util.BCryptUtil;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    
    private DAOUsuari daoUsuari;
    
    @Override
    public void init() throws ServletException {
        daoUsuari = new DAOUsuari();
        System.out.println("LoginServlet: Servlet inicialitzat correctament");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("user");
        String password = request.getParameter("pass");
        
        System.out.println("LoginServlet: Intent de login per a usuari: " + username);
        
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            System.out.println("LoginServlet: Usuari o password buits");
            response.sendRedirect("login.jsp?error=Usuari+i+password+obligatoris");
            return;
        }
        
        username = username.trim();
        
        try {
            String hashEmmagatzemat = daoUsuari.getPasswordHash(username);
            
            if (hashEmmagatzemat != null) {
                System.out.println("LoginServlet: Hash obtingut de la BD, verificant password...");
                
                if (password.equals(hashEmmagatzemat) || BCryptUtil.checkPassword(password, hashEmmagatzemat)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", username);
                    session.setAttribute("nomComplet", daoUsuari.getNomComplet(username));
                    session.setAttribute("rol", daoUsuari.getRol(username));
                    session.setMaxInactiveInterval(30 * 60);
                    
                    System.out.println("LoginServlet: ✓ Login correcte per a: " + username);
                    response.sendRedirect("menu.jsp");
                    return;
                } else {
                    System.out.println("LoginServlet: ✗ Password incorrecte per a: " + username);
                }
            } else {
                System.out.println("LoginServlet: ✗ Usuari no trobat o no actiu: " + username);
            }
            
            response.sendRedirect("login.jsp?error=Usuari+o+password+incorrecte");
            
        } catch (Exception e) {
            System.err.println("LoginServlet: ERROR en el procés de login:");
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=Error+del+servidor");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("menu.jsp");
            return;
        }
        response.sendRedirect("login.jsp");
    }
}