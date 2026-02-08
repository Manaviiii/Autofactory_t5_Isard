import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "12345");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String codi = request.getParameter("codi");
        
        if (codi == null || codi.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement("SELECT foto FROM ITEM WHERE codi_item = ?");
            ps.setString(1, codi);
            rs = ps.executeQuery();

            if (rs.next()) {
                byte[] imgData = rs.getBytes("foto");
                if (imgData != null && imgData.length > 1) {
                    response.setContentType("image/png");
                    response.setContentLength(imgData.length);
                    OutputStream out = response.getOutputStream();
                    out.write(imgData);
                    out.flush();
                } else {
                    response.sendRedirect("img/no-image.png");
                }
            } else {
                response.sendRedirect("img/no-image.png");
            }

        } catch (Exception e) {
            System.err.println("Error carregant imatge: " + e.getMessage());
            response.sendRedirect("img/no-image.png");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }
}