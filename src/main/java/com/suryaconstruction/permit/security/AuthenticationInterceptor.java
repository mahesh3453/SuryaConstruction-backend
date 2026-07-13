package com.suryaconstruction.permit.security;

import com.suryaconstruction.permit.model.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        
        // Allow preflight options requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // Allow public paths
        if (uri.startsWith("/api/auth/login") || 
            uri.startsWith("/swagger-ui") || 
            uri.startsWith("/v3/api-docs") || 
            uri.startsWith("/api-docs")) {
            return true;
        }

        // For other /api/ paths, check session
        if (uri.startsWith("/api/")) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                return sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "You must log in to access this resource.");
            }

            SessionUser user = (SessionUser) session.getAttribute("user");

            // Admin only paths
            if (uri.startsWith("/api/admin/")) {
                if (user.getRole() != Role.ADMIN) {
                    return sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden", "Access denied. Admin role required.");
                }
            }
        }

        return true;
    }

    private boolean sendErrorResponse(HttpServletResponse response, int status, String error, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"error\": \"%s\", \"message\": \"%s\"}", error, message));
        return false;
    }
}
