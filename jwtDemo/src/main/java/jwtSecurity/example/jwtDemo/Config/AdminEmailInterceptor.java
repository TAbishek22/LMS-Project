package jwtSecurity.example.jwtDemo.Config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminEmailInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AdminEmailInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String path = request.getRequestURI();

        // Exact and wildcard-based admin-only endpoints
        if (isAdminEndpoint(path)) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden: Missing or invalid token");
                return false;
            }

            String token = authHeader.substring(7);
            String email;
            try {
                email = jwtTokenProvider.getEmailFromToken(token);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden: Invalid token");
                return false;
            }

            if (!"admin@gmail.com".equalsIgnoreCase(email)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden: Only admin can access this resource");
                return false;
            }
        }

        return true;
    }

    private boolean isAdminEndpoint(String path) {
        return path.equals("/api/books/addBooks") ||
               path.equals("/api/books/bulkUpload") ||
               path.equals("/api/books/deleteAllBooks") ||
               path.matches("/api/books/\\d+") ||

               path.equals("/api/members") ||
               path.matches("/api/members/\\d+") ||
               path.matches("/api/members/email/.*") ||
               path.matches("/api/members/phone/.*") ||
               path.matches("/api/members/status/.*") ||
               path.matches("/api/members/name/.*") ||

               path.equals("/api/borrowings/with-books") ||
               path.equals("/api/borrowings/with-member") ||
               path.equals("/api/borrowings/all") ||

               path.startsWith("/api/fines") ||
               path.startsWith("/api/notifications");
    }
}