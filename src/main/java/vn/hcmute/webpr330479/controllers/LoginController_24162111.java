package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.User_24162111;
import vn.hcmute.webpr330479.services.UserService_24162111;
import vn.hcmute.webpr330479.services.impl.UserServiceImpl_24162111;

import java.io.IOException;

@WebServlet("/login")
public class LoginController_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService_24162111 service = new UserServiceImpl_24162111();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String login = trim(req.getParameter("login"));
        String password = req.getParameter("password");
        User_24162111 u = service.login(login, password);
        if (u == null) {
            req.setAttribute("error", "Sai tài khoản/mật khẩu hoặc tài khoản chưa kích hoạt.");
            req.setAttribute("loginValue", login);
            doGet(req, resp);
            return;
        }
        HttpSession s = req.getSession(true);
        req.changeSessionId();
        s.setAttribute("account", u);
        s.removeAttribute("csrfToken");
        s.removeAttribute("shopFlashMessage");
        s.removeAttribute("shopFlashType");
        s.setMaxInactiveInterval(30 * 60);
        String target = u.isAdmin() ? "/admin" : u.isSeller() ? "/seller" : "/home";
        resp.sendRedirect(req.getContextPath() + target);
    }

    private String trim(String v) {
        return v == null ? "" : v.trim();
    }
}
