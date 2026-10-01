package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.*;
import vn.hcmute.webpr330479.services.*;

import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutController_24162111 extends ShopControllerSupport_24162111 {
    private static final long serialVersionUID = 1L;
    private final ShoppingService_24162111 service = new ShoppingService_24162111();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User_24162111 account = (User_24162111) request.getSession().getAttribute("account");
        show(
                request,
                response,
                new ShippingInfo_24162111(account.getDisplayName(), account.getPhone(), "", ""));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ShippingInfo_24162111 shipping =
                new ShippingInfo_24162111(
                        request.getParameter("recipientName"),
                                request.getParameter("recipientPhone"),
                        request.getParameter("shippingAddress"), request.getParameter("orderNote"));
        try {
            String orderId =
                    service.checkout(
                            userId(request),
                            request.getParameter("cartId"),
                            request.getParameter("fingerprint"),
                            shipping);
            flash(
                    request,
                    "success",
                    "Đặt hàng COD thành công. Thanh toán tiền mặt khi nhận hàng.");
            response.sendRedirect(request.getContextPath() + "/orders/detail?id=" + orderId);
        } catch (ShopException_24162111 e) {
            request.setAttribute("error", e.getMessage());
            show(request, response, shipping);
        } catch (RuntimeException e) {
            unavailable(response, e);
        }
    }

    private void show(
            HttpServletRequest request,
            HttpServletResponse response,
            ShippingInfo_24162111 shipping)
            throws ServletException, IOException {
        try {
            CartSummary_24162111 summary = service.cart(userId(request));
            if (summary.getItems().isEmpty()) {
                flash(
                        request,
                        "warning",
                        "Giỏ hàng trống. Hãy thêm sản phẩm trước khi thanh toán.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            request.setAttribute("summary", summary);
            request.setAttribute("shipping", shipping);
            request.getRequestDispatcher("/WEB-INF/views/user/checkout.jsp")
                    .forward(request, response);
        } catch (ShopException_24162111 e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (RuntimeException e) {
            unavailable(response, e);
        }
    }
}
