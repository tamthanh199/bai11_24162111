package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.utils.FileUploadUtil_24162111;

import java.io.IOException;
import java.nio.file.*;

@WebServlet("/media")
public class MediaController_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest r, HttpServletResponse p) throws IOException {
        String type = r.getParameter("type"), name = r.getParameter("name");
        String fallback =
                "category".equals(type) ? "assets/no-category.svg" : "assets/no-product.svg";
        if (name == null || name.isBlank()) {
            p.sendRedirect(r.getContextPath() + "/" + fallback);
            return;
        }
        if (name.startsWith("assets/")) {
            p.sendRedirect(r.getContextPath() + "/" + name);
            return;
        }
        try {
            Path path =
                    "category".equals(type)
                            ? FileUploadUtil_24162111.getCategoryPath(name)
                            : FileUploadUtil_24162111.getProductPath(name);
            if (!Files.exists(path)) {
                p.sendRedirect(r.getContextPath() + "/" + fallback);
                return;
            }
            String ct = Files.probeContentType(path);
            p.setContentType(ct == null ? "application/octet-stream" : ct);
            p.setContentLengthLong(Files.size(path));
            Files.copy(path, p.getOutputStream());
        } catch (IllegalArgumentException e) {
            p.sendError(400);
        }
    }
}
