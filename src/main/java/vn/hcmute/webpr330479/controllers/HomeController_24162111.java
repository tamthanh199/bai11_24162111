package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.*;
import vn.hcmute.webpr330479.services.*;
import vn.hcmute.webpr330479.services.impl.*;

import java.io.IOException;
import java.util.*;

@WebServlet({"/home", "/products"})
public class HomeController_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final SellerService_24162111 sellerService = new SellerServiceImpl_24162111();
    private final ProductService_24162111 productService = new ProductServiceImpl_24162111();

    protected void doGet(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        List<SellerSection_24162111> sections = new ArrayList<>();
        for (Seller_24162111 s : sellerService.findAllActive()) {
            sections.add(new SellerSection_24162111(s, productService.findBySellerId(s.getId())));
        }
        r.setAttribute("sections", sections);
        r.getRequestDispatcher("/views/home.jsp").forward(r, p);
    }
}
