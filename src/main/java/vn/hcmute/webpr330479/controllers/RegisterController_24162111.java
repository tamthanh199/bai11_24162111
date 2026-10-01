package vn.hcmute.webpr330479.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.User_24162111;
import vn.hcmute.webpr330479.services.*;
import vn.hcmute.webpr330479.services.impl.*;
import vn.hcmute.webpr330479.utils.*;

import java.io.IOException;
import java.time.Instant;

@WebServlet("/register")
public class RegisterController_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService_24162111 service = new UserServiceImpl_24162111();

    protected void doGet(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        r.getRequestDispatcher("/views/register.jsp").forward(r, p);
    }

    protected void doPost(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        r.setCharacterEncoding("UTF-8");
        String username = trim(r.getParameter("username")),
                password = trim(r.getParameter("password")),
                fullName = trim(r.getParameter("fullName")),
                phone = trim(r.getParameter("phone")),
                email = trim(r.getParameter("email"));
        if (username.length() < 4
                || password.length() < 6
                || fullName.isBlank()
                || email.isBlank()) {
            r.setAttribute(
                    "error", "Vui lòng nhập đủ thông tin; username >= 4 và password >= 6 ký tự.");
            keep(r, username, fullName, phone, email);
            doGet(r, p);
            return;
        }
        if (service.existsUsername(username) || service.existsEmail(email)) {
            r.setAttribute("error", "Username hoặc email đã tồn tại.");
            keep(r, username, fullName, phone, email);
            doGet(r, p);
            return;
        }
        User_24162111 pending = new User_24162111();
        pending.setUsername(username);
        pending.setPassword(password);
        pending.setFullName(fullName);
        pending.setPhone(phone);
        pending.setEmail(email);
        pending.setImages("assets/no-user.svg");
        pending.setStatus(1);
        String otp = OtpUtil_24162111.generate();
        pending.setCode(otp);
        boolean sent = false;
        try {
            sent = EmailUtil_24162111.sendOtpIfConfigured(email, otp);
        } catch (MessagingException ignored) {
        }
        HttpSession s = r.getSession(true);
        s.setAttribute("pendingUser", pending);
        s.setAttribute("registerOtp", otp);
        s.setAttribute("registerOtpExpire", Instant.now().plusSeconds(300).toEpochMilli());
        s.setAttribute("otpMailSent", sent);
        p.sendRedirect(r.getContextPath() + "/verify-otp");
    }

    private void keep(HttpServletRequest r, String u, String n, String ph, String e) {
        r.setAttribute("username", u);
        r.setAttribute("fullName", n);
        r.setAttribute("phone", ph);
        r.setAttribute("email", e);
    }

    private String trim(String v) {
        return v == null ? "" : v.trim();
    }
}
