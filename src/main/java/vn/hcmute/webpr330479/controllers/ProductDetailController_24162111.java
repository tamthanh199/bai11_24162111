package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.Product_24162111;
import vn.hcmute.webpr330479.services.*;
import vn.hcmute.webpr330479.services.impl.*;

import java.io.IOException;

@WebServlet("/product")
public class ProductDetailController_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService_24162111 service = new ProductServiceImpl_24162111();

    protected void doGet(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        int id = parse(r.getParameter("id"), -1);
        Product_24162111 product = id > 0 ? service.findById(id) : null;
        if (product == null) {
            p.sendError(404);
            return;
        }
        r.setAttribute("product", product);
        r.getRequestDispatcher("/views/product-detail.jsp").forward(r, p);
    }

    private int parse(String v, int d) {
        try {
            return Integer.parseInt(v);
        } catch (Exception e) {
            return d;
        }
    }
}
