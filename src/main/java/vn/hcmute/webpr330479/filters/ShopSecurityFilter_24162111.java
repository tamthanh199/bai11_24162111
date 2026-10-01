package vn.hcmute.webpr330479.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.User_24162111;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

@WebFilter(urlPatterns = "/*")
public class ShopSecurityFilter_24162111 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        if (path.startsWith("/assets/") || path.equals("/media")) {
            chain.doFilter(req, res);
            return;
        }
        HttpSession session = req.getSession();
        synchronized (session) {
            if (session.getAttribute("csrfToken") == null) {
                session.setAttribute("csrfToken", UUID.randomUUID().toString());
            }
        }
        boolean protectedPath =
                path.equals("/cart")
                        || path.startsWith("/cart/")
                        || path.equals("/checkout")
                        || path.equals("/orders")
                        || path.startsWith("/orders/");
        if (protectedPath) {
            res.setHeader("Cache-Control", "no-store");
            User_24162111 account = (User_24162111) session.getAttribute("account");
            if (account == null) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            if (!account.isUser() || account.getStatus() != 1) {
                res.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Chức năng dành cho tài khoản USER đang hoạt động.");
                return;
            }
            if ("POST".equals(req.getMethod())) {
                String submitted = req.getParameter("csrfToken");
                String expected = (String) session.getAttribute("csrfToken");
                if (submitted == null
                        || !MessageDigest.isEqual(
                                expected.getBytes(StandardCharsets.UTF_8),
                                submitted.getBytes(StandardCharsets.UTF_8))) {
                    res.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "Phiên xác nhận đã hết hạn. Hãy tải lại trang và thử lại.");
                    return;
                }
            }
        }
        chain.doFilter(req, res);
    }
}
