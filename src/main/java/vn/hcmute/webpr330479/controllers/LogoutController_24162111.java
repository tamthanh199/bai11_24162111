package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutController_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest r, HttpServletResponse p) throws IOException {
        HttpSession s = r.getSession(false);
        if (s != null) {
            s.invalidate();
        }
        p.sendRedirect(r.getContextPath() + "/login");
    }
}
