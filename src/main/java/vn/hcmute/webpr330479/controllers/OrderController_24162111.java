package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.*;
import vn.hcmute.webpr330479.services.*;

import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = {"/orders", "/orders/detail"})
public class OrderController_24162111 extends ShopControllerSupport_24162111 {
    private static final long serialVersionUID = 1L;
    private final ShoppingService_24162111 service = new ShoppingService_24162111();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getServletPath().equals("/orders/detail")) {
                detail(request, response);
            } else {
                history(request, response);
            }
        } catch (ShopException_24162111 e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (IllegalArgumentException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST, "Bộ lọc hoặc trang không hợp lệ.");
        } catch (RuntimeException e) {
            unavailable(response, e);
        }
    }

    private void history(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String value = request.getParameter("status");
        Integer status = value == null || value.isBlank() ? null : Integer.valueOf(value);
        if (status != null) {
            OrderStatus_24162111.fromCode(status);
        }
        String pageValue = request.getParameter("page");
        int page = pageValue == null ? 1 : Integer.parseInt(pageValue);
        Map<Integer, Long> counts = service.statusCounts(userId(request));
        long total =
                status == null
                        ? counts.values().stream().mapToLong(Long::longValue).sum()
                        : counts.get(status);
        int totalPages =
                (int)
                        Math.max(
                                1,
                                (total + ShoppingService_24162111.PAGE_SIZE - 1)
                                        / ShoppingService_24162111.PAGE_SIZE);
        page = Math.min(totalPages, Math.max(1, page));
        request.setAttribute("orders", service.orders(userId(request), status, page));
        request.setAttribute("statuses", OrderStatus_24162111.values());
        request.setAttribute("counts", counts);
        request.setAttribute("selectedStatus", status);
        request.setAttribute("total", total);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/WEB-INF/views/user/orders.jsp").forward(request, response);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderId = request.getParameter("id");
        Cart_24162111 order = service.order(userId(request), orderId);
        if (order == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng của bạn.");
            return;
        }
        request.setAttribute("order", order);
        request.setAttribute("items", service.orderItems(userId(request), orderId));
        request.setAttribute("statuses", OrderStatus_24162111.values());
        takeFlash(request);
        request.getRequestDispatcher("/WEB-INF/views/user/order-detail.jsp")
                .forward(request, response);
    }
}
