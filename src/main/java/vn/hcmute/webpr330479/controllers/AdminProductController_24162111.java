package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.*;
import vn.hcmute.webpr330479.services.*;
import vn.hcmute.webpr330479.services.impl.*;
import vn.hcmute.webpr330479.utils.FileUploadUtil_24162111;

import java.io.IOException;

@WebServlet(
        urlPatterns = {
            "/admin/products",
            "/admin/products/form",
            "/admin/products/save",
            "/admin/products/delete"
        })
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024)
public class AdminProductController_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int SIZE = 6;
    private final ProductService_24162111 service = new ProductServiceImpl_24162111();
    private final CategoryService_24162111 categoryService = new CategoryServiceImpl_24162111();
    private final SellerService_24162111 sellerService = new SellerServiceImpl_24162111();

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
        int page = Math.max(1, parseInt(r.getParameter("page"), 1));
        long total = service.count();
        int totalPages = (int) Math.max(1, (total + SIZE - 1) / SIZE);
        if (page > totalPages) {
            page = totalPages;
        }
        r.setAttribute("products", service.findPage(page, SIZE));
        r.setAttribute("page", page);
        r.setAttribute("totalPages", totalPages);
        r.getRequestDispatcher("/views/admin/product-list.jsp").forward(r, p);
    }

    private void form(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        int id = parseInt(r.getParameter("id"), 0);
        Product_24162111 product = id > 0 ? service.findById(id) : new Product_24162111();
        if (product == null) {
            p.sendRedirect(r.getContextPath() + "/admin/products");
            return;
        }
        r.setAttribute("product", product);
        r.setAttribute("categories", categoryService.findAll());
        r.setAttribute("sellers", sellerService.findAllActive());
        r.getRequestDispatcher("/views/admin/product-form.jsp").forward(r, p);
    }

    private void save(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        r.setCharacterEncoding("UTF-8");
        int id = parseInt(r.getParameter("id"), 0);
        Product_24162111 product = id > 0 ? service.findById(id) : new Product_24162111();
        if (product == null) {
            p.sendRedirect(r.getContextPath() + "/admin/products");
            return;
        }
        String old = product.getImages();
        String name = trim(r.getParameter("name"));
        int categoryId = parseInt(r.getParameter("categoryId"), -1),
                sellerId = parseInt(r.getParameter("sellerId"), -1);
        Category_24162111 category = categoryService.findById(categoryId);
        Seller_24162111 seller = sellerService.findById(sellerId);
        if (name.isBlank() || category == null || seller == null) {
            r.setAttribute("error", "Tên sản phẩm, danh mục và seller là bắt buộc.");
            r.setAttribute("product", product);
            r.setAttribute("categories", categoryService.findAll());
            r.setAttribute("sellers", sellerService.findAllActive());
            r.getRequestDispatcher("/views/admin/product-form.jsp").forward(r, p);
            return;
        }
        product.setName(name);
        product.setCode(parseLong(r.getParameter("code"), System.currentTimeMillis()));
        product.setCategory(category);
        product.setSeller(seller);
        product.setDescription(trim(r.getParameter("description")));
        product.setPrice(Math.max(0, parseDouble(r.getParameter("price"), 0)));
        product.setAmount(Math.max(0, parseInt(r.getParameter("amount"), 0)));
        product.setStock(Math.max(0, parseInt(r.getParameter("stock"), 0)));
        product.setWishlist(Math.max(0, parseInt(r.getParameter("wishlist"), 0)));
        product.setStatus(parseInt(r.getParameter("status"), 1) == 0 ? 0 : 1);
        String newImage = FileUploadUtil_24162111.saveProductImage(r.getPart("imageFile"));
        if (newImage != null) {
            product.setImages(newImage);
        } else if (old == null || old.isBlank()) {
            product.setImages("assets/no-product.svg");
        }
        service.save(product);
        if (newImage != null) {
            FileUploadUtil_24162111.deleteProductImage(old);
        }
        p.sendRedirect(r.getContextPath() + "/admin/products");
    }

    private void delete(HttpServletRequest r, HttpServletResponse p) throws IOException {
        int id = parseInt(r.getParameter("id"), -1);
        if (id > 0) {
            Product_24162111 product = service.findById(id);
            try {
                service.delete(id);
                if (product != null)
                    FileUploadUtil_24162111.deleteProductImage(product.getImages());
            } catch (RuntimeException e) {
                p.sendRedirect(r.getContextPath() + "/admin/products?error=used");
                return;
            }
        }
        p.sendRedirect(r.getContextPath() + "/admin/products");
    }

    private int parseInt(String v, int d) {
        try {
            return Integer.parseInt(v);
        } catch (Exception e) {
            return d;
        }
    }

    private long parseLong(String v, long d) {
        try {
            return Long.parseLong(v);
        } catch (Exception e) {
            return d;
        }
    }

    private double parseDouble(String v, double d) {
        try {
            return Double.parseDouble(v);
        } catch (Exception e) {
            return d;
        }
    }

    private String trim(String v) {
        return v == null ? "" : v.trim();
    }
}
