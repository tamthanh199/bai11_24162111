package vn.hcmute.webpr330479.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.User_24162111;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminAuthorizationFilter_24162111 implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse p = (HttpServletResponse) res;
        HttpSession s = r.getSession(false);
        User_24162111 u = s == null ? null : (User_24162111) s.getAttribute("account");
        if (u == null) {
            p.sendRedirect(r.getContextPath() + "/login");
            return;
        }
        if (!u.isAdmin()) {
            p.sendRedirect(r.getContextPath() + (u.isSeller() ? "/seller" : "/home"));
            return;
        }
        chain.doFilter(r, p);
    }
}
