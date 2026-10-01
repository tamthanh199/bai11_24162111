package vn.hcmute.webpr330479.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;

import vn.hcmute.webpr330479.config.JPAConfig_24162111;
import vn.hcmute.webpr330479.models.*;
import vn.hcmute.webpr330479.services.ShopException_24162111;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ShoppingDaoImpl_24162111 {
    private final Supplier<EntityManager> entityManagers;

    public ShoppingDaoImpl_24162111() {
        this(JPAConfig_24162111::getEntityManager);
    }

    public ShoppingDaoImpl_24162111(Supplier<EntityManager> entityManagers) {
        this.entityManagers = entityManagers;
    }

    public <T> T transaction(
            int userId, boolean lockUser, BiFunction<EntityManager, User_24162111, T> action) {
        EntityManager em = entityManagers.get();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User_24162111 user =
                    lockUser
                            ? em.find(User_24162111.class, userId, LockModeType.PESSIMISTIC_WRITE)
                            : em.find(User_24162111.class, userId);
            if (user == null || !user.isUser() || user.getStatus() != 1) {
                throw new ShopException_24162111("Tài khoản USER không hợp lệ hoặc đã bị khóa.");
            }
            T result = action.apply(em, user);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Giữ khóa user khi gọi hàm này để tránh tạo hai giỏ cùng lúc.
    public Cart_24162111 activeCart(EntityManager em, User_24162111 user) {
        List<Cart_24162111> carts =
                em.createQuery(
                                "select c from Cart_24162111 c where c.user.id = :userId and"
                                        + " c.status = -1",
                                Cart_24162111.class)
                        .setParameter("userId", user.getId())
                        .getResultList();
        if (!carts.isEmpty()) {
            return carts.get(0);
        }
        Cart_24162111 cart = new Cart_24162111();
        cart.setUser(user);
        em.persist(cart);
        em.flush();
        return cart;
    }

    public Cart_24162111 ownedCart(EntityManager em, int userId, String cartId) {
        if (cartId == null || cartId.length() > 50) {
            return null;
        }
        return em.createQuery(
                        "select c from Cart_24162111 c where c.id = :id and c.user.id = :userId",
                        Cart_24162111.class)
                .setParameter("id", cartId)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<CartItem_24162111> items(EntityManager em, String cartId) {
        return em.createQuery(
                        "select i from CartItem_24162111 i where i.cart.id = :cartId order by"
                                + " i.product.id",
                        CartItem_24162111.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    public CartItem_24162111 ownedItem(EntityManager em, String cartId, String itemId) {
        return em.createQuery(
                        "select i from CartItem_24162111 i where i.id = :id and i.cart.id ="
                                + " :cartId",
                        CartItem_24162111.class)
                .setParameter("id", itemId)
                .setParameter("cartId", cartId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public Product_24162111 lockedProduct(EntityManager em, int productId) {

        Product_24162111 product = em.find(Product_24162111.class, productId);
        if (product != null) {
            // Đọc lại tồn kho trong khóa, kể cả khi entity đã được tải trước đó.
            em.refresh(product, LockModeType.PESSIMISTIC_WRITE);
        }
        return product;
    }
}
