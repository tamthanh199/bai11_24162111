package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.Category_24162111;
import vn.hcmute.webpr330479.services.*;
import vn.hcmute.webpr330479.services.impl.*;
import vn.hcmute.webpr330479.utils.FileUploadUtil_24162111;

import java.io.IOException;

@WebServlet(
        urlPatterns = {
            "/admin/categories",
            "/admin/categories/form",
            "/admin/categories/save",
            "/admin/categories/delete"
        })
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024)
public class AdminCategoryController_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int SIZE = 6;
    private final CategoryService_24162111 service = new CategoryServiceImpl_24162111();

    protected void doGet(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        String path = r.getServletPath();
        if (path.endsWith("/form")) {
            form(r, p);
            return;
        }
        if (path.endsWith("/delete")) {
            delete(r, p);
            return;
        }
        list(r, p);
    }

    protected void doPost(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        save(r, p);
    }

    private void list(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        int page = Math.max(1, parse(r.getParameter("page"), 1));
        long total = service.count();
        int totalPages = (int) Math.max(1, (total + SIZE - 1) / SIZE);
        if (page > totalPages) {
            page = totalPages;
        }
        r.setAttribute("categories", service.findPage(page, SIZE));
        r.setAttribute("page", page);
        r.setAttribute("totalPages", totalPages);
        r.getRequestDispatcher("/views/admin/category-list.jsp").forward(r, p);
    }

    private void form(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        int id = parse(r.getParameter("id"), 0);
        Category_24162111 c = id > 0 ? service.findById(id) : new Category_24162111();
        if (c == null) {
            p.sendRedirect(r.getContextPath() + "/admin/categories");
            return;
        }
        r.setAttribute("category", c);
        r.getRequestDispatcher("/views/admin/category-form.jsp").forward(r, p);
    }

    private void save(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        r.setCharacterEncoding("UTF-8");
        int id = parse(r.getParameter("id"), 0);
        Category_24162111 c = id > 0 ? service.findById(id) : new Category_24162111();
        if (c == null) {
            p.sendRedirect(r.getContextPath() + "/admin/categories");
            return;
        }
        String old = c.getImages();
        String name = trim(r.getParameter("name"));
        if (name.isBlank()) {
            r.setAttribute("error", "Tên danh mục không được để trống.");
            r.setAttribute("category", c);
            r.getRequestDispatcher("/views/admin/category-form.jsp").forward(r, p);
            return;
        }
        c.setName(name);
        c.setStatus(parse(r.getParameter("status"), 1) == 0 ? 0 : 1);
        String newImage = FileUploadUtil_24162111.saveCategoryImage(r.getPart("imageFile"));
        if (newImage != null) {
            c.setImages(newImage);
        } else if (old == null || old.isBlank()) {
            c.setImages("assets/no-category.svg");
        }
        service.save(c);
        if (newImage != null) {
            FileUploadUtil_24162111.deleteCategoryImage(old);
        }
        p.sendRedirect(r.getContextPath() + "/admin/categories");
    }

    private void delete(HttpServletRequest r, HttpServletResponse p) throws IOException {
        int id = parse(r.getParameter("id"), -1);
        if (id > 0) {
            Category_24162111 c = service.findById(id);
            try {
                service.delete(id);
                if (c != null) {
                    FileUploadUtil_24162111.deleteCategoryImage(c.getImages());
                }
            } catch (RuntimeException e) {
                p.sendRedirect(r.getContextPath() + "/admin/categories?error=used");
                return;
            }
        }
        p.sendRedirect(r.getContextPath() + "/admin/categories");
    }

    private int parse(String v, int d) {
        try {
            return Integer.parseInt(v);
        } catch (Exception e) {
            return d;
        }
    }

    private String trim(String v) {
        return v == null ? "" : v.trim();
    }
}
