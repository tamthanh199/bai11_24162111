package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.User_24162111;
import vn.hcmute.webpr330479.services.*;
import vn.hcmute.webpr330479.services.impl.*;

import java.io.IOException;
import java.time.Instant;

@WebServlet("/verify-otp")
public class VerifyOtpController_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService_24162111 service = new UserServiceImpl_24162111();

    protected void doGet(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        HttpSession s = r.getSession(false);
        if (s == null || s.getAttribute("pendingUser") == null) {
            p.sendRedirect(r.getContextPath() + "/register");
            return;
        }
        r.getRequestDispatcher("/views/verify-otp.jsp").forward(r, p);
    }

    protected void doPost(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        HttpSession s = r.getSession(false);
        if (s == null) {
            p.sendRedirect(r.getContextPath() + "/register");
            return;
        }
        String expected = (String) s.getAttribute("registerOtp");
        Long expire = (Long) s.getAttribute("registerOtpExpire");
        User_24162111 pending = (User_24162111) s.getAttribute("pendingUser");
        String entered = r.getParameter("otp");
        if (expected == null
                || expire == null
                || pending == null
                || Instant.now().toEpochMilli() > expire) {
            r.setAttribute("error", "OTP đã hết hạn. Vui lòng đăng ký lại.");
            doGet(r, p);
            return;
        }
        if (!expected.equals(entered == null ? "" : entered.trim())) {
            r.setAttribute("error", "OTP không đúng.");
            doGet(r, p);
            return;
        }
        service.register(pending);
        s.removeAttribute("pendingUser");
        s.removeAttribute("registerOtp");
        s.removeAttribute("registerOtpExpire");
        s.removeAttribute("otpMailSent");
        p.sendRedirect(r.getContextPath() + "/login?registered=1");
    }
}
