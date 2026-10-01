package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.services.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/remove", "/cart/clear"})
public class CartController_24162111 extends ShopControllerSupport_24162111 {
    private static final long serialVersionUID = 1L;
    private final ShoppingService_24162111 service = new ShoppingService_24162111();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getServletPath().equals("/cart")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        try {
            request.setAttribute("summary", service.cart(userId(request)));
            takeFlash(request);
            request.getRequestDispatcher("/WEB-INF/views/user/cart.jsp").forward(request, response);
        } catch (ShopException_24162111 e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (RuntimeException e) {
            unavailable(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int userId = userId(request);
            switch (request.getServletPath()) {
                case "/cart/add" ->
                        service.add(
                                userId,
                                positiveInt(request.getParameter("productId")),
                                CheckoutRules_24162111.parseQuantity(
                                        request.getParameter("quantity")));
                case "/cart/update" ->
                        service.update(
                                userId,
                                request.getParameter("itemId"),
                                CheckoutRules_24162111.parseQuantity(
                                        request.getParameter("quantity")));
                case "/cart/remove" -> service.remove(userId, request.getParameter("itemId"));
                case "/cart/clear" -> service.clear(userId);
                default -> {
                    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    return;
                }
            }
            flash(request, "success", "Đã cập nhật giỏ hàng.");
        } catch (ShopException_24162111 e) {
            flash(request, "error", e.getMessage());
        } catch (RuntimeException e) {
            getServletContext().log("Cart mutation failed", e);
            flash(request, "error", "Chưa thể cập nhật giỏ hàng. Vui lòng thử lại.");
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
