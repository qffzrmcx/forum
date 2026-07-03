package com.qffz.filter;

import com.qffz.utils.JwtUtil;
import com.qffz.utils.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Order(1)
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        try {
            String token = httpRequest.getHeader("token");
            if (token == null || token.isBlank()) {
                token = httpRequest.getHeader("Authorization");
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }
            }
            if (token != null && !token.isBlank()) {
                Map<String, Object> payload = JwtUtil.parseToken(token);
                if (payload != null) {
                    UserContext.set(new UserContext.LoginUser(
                            ((Number) payload.get("userId")).longValue(),
                            String.valueOf(payload.get("username")),
                            String.valueOf(payload.get("role"))
                    ));
                }
            }
            if (requiresLogin(path, method) && UserContext.get() == null) {
                writeUnauthorized(httpResponse, "请先登录");
                return;
            }
            if (path.startsWith("/admin") && !UserContext.isAdmin()) {
                writeUnauthorized(httpResponse, "无管理员权限");
                return;
            }
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }

    private boolean requiresLogin(String path, String method) {
        if ("OPTIONS".equalsIgnoreCase(method)) return false;
        if (path.equals("/user/login") || path.equals("/user/register")) return false;
        if (path.equals("/section/list")) return false;
        if (path.startsWith("/post/list") || path.startsWith("/post/search") || path.startsWith("/post/detail/")) return false;
        if (path.startsWith("/comment/list")) return false;
        return path.startsWith("/user") || path.startsWith("/post") || path.startsWith("/comment")
                || path.startsWith("/like") || path.startsWith("/collect") || path.startsWith("/admin")
                || path.startsWith("/section");
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":400,\"msg\":\"" + message + "\",\"data\":null}");
    }
}
