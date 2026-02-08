package filter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthFilter inicialitzat");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = uri.substring(contextPath.length());
        
        boolean esRecursPublic = 
            path.equals("/") ||
            path.equals("/login.jsp") ||
            path.equals("/LoginServlet") ||
            path.equals("/index.html") ||
            path.startsWith("/css/") ||
            path.startsWith("/img/") ||
            path.startsWith("/js/") ||
            path.endsWith(".css") ||
            path.endsWith(".png") ||
            path.endsWith(".jpg") ||
            path.endsWith(".ico");
        
        if (esRecursPublic) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpSession session = httpRequest.getSession(false);
        boolean autenticat = (session != null && session.getAttribute("user") != null);
        
        if (autenticat) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(contextPath + "/login.jsp");
        }
    }
    
    @Override
    public void destroy() {}
}
