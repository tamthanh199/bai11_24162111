package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin")
public class AdminHomeController_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        r.getRequestDispatcher("/views/admin/index.jsp").forward(r, p);
    }
}
