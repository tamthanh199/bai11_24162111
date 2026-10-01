package vn.hcmute.webpr330479.services;

import jakarta.persistence.EntityManager;

import vn.hcmute.webpr330479.dao.impl.ShoppingDaoImpl_24162111;
import vn.hcmute.webpr330479.models.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShoppingService_24162111 {
    private static final int MAX_CART_LINES = 100;
    public static final int PAGE_SIZE = 6;
    private final ShoppingDaoImpl_24162111 dao;

    public ShoppingService_24162111() {
        this(new ShoppingDaoImpl_24162111());
    }

    public ShoppingService_24162111(ShoppingDaoImpl_24162111 dao) {
        this.dao = dao;
    }

    public CartSummary_24162111 cart(int userId) {
        return dao.transaction(
                userId,
                true,
                (em, user) -> {
                    Cart_24162111 cart = dao.activeCart(em, user);
                    List<CartItem_24162111> items = dao.items(em, cart.getId());

                    // Tách entity trước khi hiển thị giá hiện tại để giữ nguyên giá đã lưu.
                    items.forEach(em::detach);
                    for (CartItem_24162111 item : items) {
                        item.setUnitPrice(CheckoutRules_24162111.price(item.getProduct()));
                    }
                    return new CartSummary_24162111(cart, items);
                });
    }

    public void add(int userId, int productId, int quantity) {
        dao.transaction(
                userId,
                true,
                (em, user) -> {
                    Cart_24162111 cart = dao.activeCart(em, user);
                    Product_24162111 product = dao.lockedProduct(em, productId);
                    CheckoutRules_24162111.requireQuantity(product, quantity);
                    List<CartItem_24162111> items = dao.items(em, cart.getId());
                    CartItem_24162111 item =
                            items.stream()
                                    .filter(line -> line.getProduct().getId() == productId)
                                    .findFirst()
                                    .orElse(null);
                    if (item == null && items.size() >= MAX_CART_LINES) {
                        throw new ShopException_24162111("Giỏ hàng tối đa 100 sản phẩm khác nhau.");
                    }
                    int combined = item == null ? quantity : item.getQuantity() + quantity;
                    CheckoutRules_24162111.requireQuantity(product, combined);
                    boolean newItem = item == null;
                    if (newItem) {
                        item = new CartItem_24162111();
                        item.setCart(cart);
                        item.setProduct(product);
                    }
                    item.setQuantity(combined);
                    item.setUnitPrice(CheckoutRules_24162111.price(product));

                    // Gán số lượng trước persist để thỏa ràng buộc 1–99.
                    if (newItem) {
                        em.persist(item);
                    }
                    return null;
                });
    }

    public void update(int userId, String itemId, int quantity) {
        dao.transaction(
                userId,
                true,
                (em, user) -> {
                    Cart_24162111 cart = dao.activeCart(em, user);
                    CartItem_24162111 item = requireItem(em, cart, itemId);
                    Product_24162111 product = dao.lockedProduct(em, item.getProduct().getId());
                    CheckoutRules_24162111.requireQuantity(product, quantity);
                    item.setQuantity(quantity);
                    item.setUnitPrice(CheckoutRules_24162111.price(product));
                    return null;
                });
    }

    public void remove(int userId, String itemId) {
        dao.transaction(
                userId,
                true,
                (em, user) -> {
                    Cart_24162111 cart = dao.activeCart(em, user);
                    em.remove(requireItem(em, cart, itemId));
                    return null;
                });
    }

    public void clear(int userId) {
        dao.transaction(
                userId,
                true,
                (em, user) -> {
                    Cart_24162111 cart = dao.activeCart(em, user);
                    dao.items(em, cart.getId()).forEach(em::remove);
                    return null;
                });
    }

    private CartItem_24162111 requireItem(EntityManager em, Cart_24162111 cart, String itemId) {
        CartItem_24162111 item = dao.ownedItem(em, cart.getId(), itemId);
        if (item == null) {
            throw new ShopException_24162111("Sản phẩm không thuộc giỏ hàng hiện tại của bạn.");
        }
        return item;
    }

    public String checkout(
            int userId, String cartId, String fingerprint, ShippingInfo_24162111 shipping) {
        return dao.transaction(
                userId,
                true,
                (em, user) -> {
                    Cart_24162111 cart = dao.ownedCart(em, userId, cartId);
                    if (cart == null) {
                        throw new ShopException_24162111(
                                "Giỏ hàng không tồn tại hoặc không thuộc tài khoản của bạn.");
                    }

                    if (cart.getStatus() >= 0
                            && cart.getStatus() <= 7
                            && "COD".equals(cart.getPaymentMethod())) {
                        return cart.getId();
                    }
                    if (cart.getStatus() != OrderStatus_24162111.ACTIVE_CART) {
                        throw new ShopException_24162111("Giỏ hàng này không thể thanh toán.");
                    }
                    shipping.validate();
                    List<CartItem_24162111> items = dao.items(em, cart.getId());
                    if (items.isEmpty()) {
                        throw new ShopException_24162111(
                                "Giỏ hàng đang trống. Hãy chọn sản phẩm trước khi đặt hàng.");
                    }

                    // Khóa sản phẩm theo thứ tự ID để hạn chế deadlock.
                    for (CartItem_24162111 item : items) {
                        Product_24162111 product = dao.lockedProduct(em, item.getProduct().getId());
                        CheckoutRules_24162111.requireQuantity(product, item.getQuantity());
                        item.setUnitPrice(CheckoutRules_24162111.price(product));
                        item.setProductName(product.getName());
                        item.setProductCode(product.getCode());
                        item.setProductImage(product.getImages());
                    }
                    if (!CheckoutRules_24162111.fingerprint(items).equals(fingerprint)) {
                        throw new ShopException_24162111(
                                "Giỏ hàng hoặc giá đã thay đổi. Hãy xem lại rồi xác nhận đặt"
                                        + " hàng.");
                    }
                    BigDecimal total =
                            items.stream()
                                    .map(CartItem_24162111::getLineTotal)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (total.compareTo(new BigDecimal("99999999999999999.99")) > 0) {
                        throw new ShopException_24162111(
                                "Giá trị đơn hàng vượt giới hạn cho phép.");
                    }
                    for (CartItem_24162111 item : items) {
                        Product_24162111 product = item.getProduct();
                        product.setStock(product.getStock() - item.getQuantity());
                    }
                    cart.setRecipientName(shipping.getName());
                    cart.setRecipientPhone(shipping.getPhone());
                    cart.setShippingAddress(shipping.getAddress());
                    cart.setOrderNote(shipping.getNote());
                    cart.setPaymentMethod("COD");
                    cart.setTotalAmount(total);
                    cart.setBuyDate(LocalDateTime.now());
                    cart.setStatus(OrderStatus_24162111.NEW.getCode());
                    em.flush();
                    return cart.getId();
                });
    }

    public Cart_24162111 order(int userId, String orderId) {
        return dao.transaction(
                userId,
                false,
                (em, user) -> {
                    Cart_24162111 order = dao.ownedCart(em, userId, orderId);
                    return order != null && order.getStatus() >= 0 && order.getStatus() <= 7
                            ? order
                            : null;
                });
    }

    public List<CartItem_24162111> orderItems(int userId, String orderId) {
        return dao.transaction(
                userId,
                false,
                (em, user) -> {
                    Cart_24162111 order = dao.ownedCart(em, userId, orderId);
                    if (order == null || order.getStatus() < 0 || order.getStatus() > 7) {
                        throw new ShopException_24162111("Không tìm thấy đơn hàng của bạn.");
                    }
                    return dao.items(em, order.getId());
                });
    }

    public Map<Integer, Long> statusCounts(int userId) {
        return dao.transaction(
                userId,
                false,
                (em, user) -> {
                    Map<Integer, Long> counts = new LinkedHashMap<>();
                    for (OrderStatus_24162111 status : OrderStatus_24162111.values()) {
                        counts.put(status.getCode(), 0L);
                    }
                    List<Object[]> rows =
                            em.createQuery(
                                            "select c.status, count(c) from Cart_24162111 c where"
                                                + " c.user.id = :userId and c.status between 0 and"
                                                + " 7 group by c.status",
                                            Object[].class)
                                    .setParameter("userId", userId)
                                    .getResultList();
                    for (Object[] row : rows) {
                        counts.put(((Number) row[0]).intValue(), ((Number) row[1]).longValue());
                    }
                    return counts;
                });
    }

    public List<Cart_24162111> orders(int userId, Integer status, int page) {
        if (status != null) {
            OrderStatus_24162111.fromCode(status);
        }
        return dao.transaction(
                userId,
                false,
                (em, user) ->
                        em.createQuery(
                                        "select c from Cart_24162111 c where c.user.id = :userId"
                                                + " and c.status between 0 and 7"
                                                + (status == null
                                                        ? ""
                                                        : " and c.status = " + status)
                                                + " order by c.buyDate desc, c.id desc",
                                        Cart_24162111.class)
                                .setParameter("userId", userId)
                                .setFirstResult((Math.max(1, page) - 1) * PAGE_SIZE)
                                .setMaxResults(PAGE_SIZE)
                                .getResultList());
    }
}
