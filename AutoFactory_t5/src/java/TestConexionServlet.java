import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import util.DBConnection;

@WebServlet("/TestConexion")
public class TestConexionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        
        try {
            // Provar connexió amb DBConnection
            Connection con = DBConnection.getConnection();
            
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h2 style='color:green;'>✓ Connexió exitosa a la base de dades!</h2>");
            
            // Provar si existeix la taula USUARIS
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM USUARIS");
                
                if (rs.next()) {
                    int total = rs.getInt(1);
                    response.getWriter().println("<p><strong>Total usuaris a la BD:</strong> " + total + "</p>");
                    
                    // Llistar usuaris
                    rs = st.executeQuery("SELECT username, nom_complet, rol, actiu FROM USUARIS ORDER BY username");
                    response.getWriter().println("<h3>Usuaris disponibles:</h3>");
                    response.getWriter().println("<table border='1' cellpadding='5' cellspacing='0'>");
                    response.getWriter().println("<tr><th>Username</th><th>Nom Complet</th><th>Rol</th><th>Actiu</th></tr>");
                    
                    while (rs.next()) {
                        String actiu = rs.getString("actiu");
                        String color = actiu.equals("s") ? "green" : "red";
                        response.getWriter().println("<tr>");
                        response.getWriter().println("<td><strong>" + rs.getString("username") + "</strong></td>");
                        response.getWriter().println("<td>" + rs.getString("nom_complet") + "</td>");
                        response.getWriter().println("<td>" + rs.getString("rol") + "</td>");
                        response.getWriter().println("<td style='color:" + color + ";'>" + actiu + "</td>");
                        response.getWriter().println("</tr>");
                    }
                    response.getWriter().println("</table>");
                    
                    response.getWriter().println("<br><p><em>Nota: Les contrasenyes estan encriptades amb BCrypt</em></p>");
                    response.getWriter().println("<p>Per fer login, usa:</p>");
                    response.getWriter().println("<ul>");
                    response.getWriter().println("<li>Usuario: <strong>admin</strong> / Password: <strong>admin</strong></li>");
                    response.getWriter().println("<li>Usuario: <strong>user1</strong> / Password: <strong>password</strong></li>");
                    response.getWriter().println("</ul>");
                }
                
                rs.close();
                st.close();
            } catch (Exception e) {
                response.getWriter().println("<p style='color:red;'><strong>⚠ Error consultant taula USUARIS:</strong> " + e.getMessage() + "</p>");
                response.getWriter().println("<p>Pot ser que no existeixi la taula USUARIS.</p>");
                response.getWriter().println("<p><strong>Solució:</strong> Executa el fitxer <code>SQL/usuaris.sql</code> a SQL Developer</p>");
                response.getWriter().println("<pre>");
                e.printStackTrace(response.getWriter());
                response.getWriter().println("</pre>");
            }
            
            con.close();
            response.getWriter().println("</body></html>");

        } catch (Exception e) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h2 style='color:red;'>✗ Error de connexió a la base de dades</h2>");
            response.getWriter().println("<p><strong>Error:</strong> " + e.getMessage() + "</p>");
            response.getWriter().println("<h3>Detalls de l'error:</h3>");
            response.getWriter().println("<pre style='background-color:#f0f0f0; padding:10px; border:1px solid #ccc;'>");
            e.printStackTrace(response.getWriter());
            response.getWriter().println("</pre>");
            
            response.getWriter().println("<h3 style='color:#ff6600;'>Comprova els següents punts:</h3>");
            response.getWriter().println("<ol>");
            response.getWriter().println("<li>Oracle està executant-se a la màquina virtual ISARD?</li>");
            response.getWriter().println("<li>Les credencials són correctes: <strong>alumne/alumne</strong>?</li>");
            response.getWriter().println("<li>L'URL de connexió apunta a la IP correcta?</li>");
            response.getWriter().println("<li>El port 1521 està obert i accessible?</li>");
            response.getWriter().println("<li>El SID/Service Name és correcte (xe o XEPDB1)?</li>");
            response.getWriter().println("<li>El driver JDBC d'Oracle (ojdbc) està al WEB-INF/lib?</li>");
            response.getWriter().println("</ol>");
            
            response.getWriter().println("<h3>Fitxers de configuració a revisar:</h3>");
            response.getWriter().println("<ul>");
            response.getWriter().println("<li><code>AutoFactory_t5/src/conf/db.properties</code></li>");
            response.getWriter().println("<li><code>P1-T4-DAOOracle/src/db.properties</code></li>");
            response.getWriter().println("</ul>");
            
            response.getWriter().println("</body></html>");
        }
    }
}