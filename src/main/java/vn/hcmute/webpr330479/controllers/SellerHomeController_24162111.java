package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.hcmute.webpr330479.models.Seller_24162111;
import vn.hcmute.webpr330479.models.User_24162111;
import vn.hcmute.webpr330479.services.ProductService_24162111;
import vn.hcmute.webpr330479.services.impl.ProductServiceImpl_24162111;

import java.io.IOException;

@WebServlet("/seller")
public class SellerHomeController_24162111 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService_24162111 service = new ProductServiceImpl_24162111();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        User_24162111 user =
                session == null ? null : (User_24162111) session.getAttribute("account");

        if (user == null || !user.isSeller() || user.getSeller() == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Seller_24162111 seller = user.getSeller();

        request.setAttribute("sellerInfo", seller);
        request.setAttribute("products", service.findBySellerId(seller.getId()));

        request.getRequestDispatcher("/views/seller-home.jsp").forward(request, response);
    }
}
