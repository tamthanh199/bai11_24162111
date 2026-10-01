package vn.hcmute.webpr330479.controllers;

import jakarta.servlet.http.*;

import vn.hcmute.webpr330479.models.User_24162111;
import vn.hcmute.webpr330479.services.ShopException_24162111;

import java.io.IOException;

public abstract class ShopControllerSupport_24162111 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected int userId(HttpServletRequest request) {
        return ((User_24162111) request.getSession().getAttribute("account")).getId();
    }

    protected int positiveInt(String value) {
        try {
            int number = Integer.parseInt(value);
            if (number > 0) {
                return number;
            }
        } catch (NumberFormatException ignored) {

        }
        throw new ShopException_24162111("Mã sản phẩm không hợp lệ.");
    }

    protected void flash(HttpServletRequest request, String type, String message) {
        request.getSession().setAttribute("shopFlashType", type);
        request.getSession().setAttribute("shopFlashMessage", message);
    }

    protected void takeFlash(HttpServletRequest request) {
        HttpSession session = request.getSession();
        request.setAttribute("flashType", session.getAttribute("shopFlashType"));
        request.setAttribute("flashMessage", session.getAttribute("shopFlashMessage"));
        session.removeAttribute("shopFlashType");
        session.removeAttribute("shopFlashMessage");
    }

    protected void unavailable(HttpServletResponse response, RuntimeException error)
            throws IOException {
        getServletContext().log("Cart/order operation failed", error);
        response.sendError(
                HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                "Chưa thể xử lý lúc này. Vui lòng thử lại; nếu vẫn lỗi, liên hệ quản trị viên.");
    }
}
