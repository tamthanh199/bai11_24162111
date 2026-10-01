package vn.hcmute.webpr330479;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.*;

import org.junit.jupiter.api.*;

import vn.hcmute.webpr330479.dao.impl.ShoppingDaoImpl_24162111;
import vn.hcmute.webpr330479.models.*;
import vn.hcmute.webpr330479.services.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

class ShoppingServiceTest_24162111 {
    private static EntityManagerFactory factory;
    private ShoppingService_24162111 shop;
    private int userId;
    private int otherUserId;
    private int productId;

    @BeforeAll
    static void start() {
        factory =
                Persistence.createEntityManagerFactory(
                        "de05-jpa",
                        Map.of(
                                "jakarta.persistence.jdbc.url",
                                        "jdbc:h2:mem:shopping;MODE=MSSQLServer;DB_CLOSE_DELAY=-1;INIT=CREATE"
                                            + " SCHEMA IF NOT EXISTS dbo",
                                "jakarta.persistence.jdbc.driver", "org.h2.Driver",
                                "jakarta.persistence.jdbc.user", "sa",
                                "jakarta.persistence.jdbc.password", "",
                                "hibernate.dialect", "org.hibernate.dialect.H2Dialect",
                                "hibernate.hbm2ddl.auto", "create-drop"));
    }

    @AfterAll
    static void stop() {
        if (factory != null) {
            factory.close();
        }
    }

    @BeforeEach
    void seed() {
        shop =
                new ShoppingService_24162111(
                        new ShoppingDaoImpl_24162111(factory::createEntityManager));
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        UserRole_24162111 role = new UserRole_24162111();
        role.setRoleName("USER");
        em.persist(role);
        User_24162111 user = newUser(role);
        User_24162111 other = newUser(role);
        em.persist(user);
        em.persist(other);
        Category_24162111 category = new Category_24162111();
        category.setName("Demo category");
        em.persist(category);
        Seller_24162111 seller = new Seller_24162111();
        seller.setName("Demo seller");
        seller.setStatus(1);
        em.persist(seller);
        Product_24162111 product = new Product_24162111();
        product.setName("Sản phẩm kiểm thử");
        product.setCategory(category);
        product.setSeller(seller);
        product.setPrice(1000.50);
        product.setStock(10);
        product.setAmount(10);
        product.setCode(10001L);
        product.setImages("assets/no-product.svg");
        em.persist(product);
        em.getTransaction().commit();
        userId = user.getId();
        otherUserId = other.getId();
        productId = product.getId();
        em.close();
    }

    private User_24162111 newUser(UserRole_24162111 role) {
        User_24162111 user = new User_24162111();
        user.setUsername(UUID.randomUUID().toString());
        user.setFullName("Người nhận");
        user.setPhone("0901234567");
        user.setRole(role);
        user.setStatus(1);
        return user;
    }

    private ShippingInfo_24162111 shipping() {
        return new ShippingInfo_24162111(
                "Nguyễn Văn A",
                "0901234567",
                "123 Đường A, Thành phố Hồ Chí Minh",
                "Gọi trước khi giao");
    }

    private void changeProduct(java.util.function.Consumer<Product_24162111> change) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        change.accept(em.find(Product_24162111.class, productId));
        em.getTransaction().commit();
        em.close();
    }

    private int stock() {
        EntityManager em = factory.createEntityManager();
        int stock = em.find(Product_24162111.class, productId).getStock();
        em.close();
        return stock;
    }

    private String checkout(CartSummary_24162111 summary) {
        return shop.checkout(
                userId, summary.getCart().getId(), summary.getFingerprint(), shipping());
    }

    @Test
    void addMergesUpdateRemoveAndClear() {
        shop.add(userId, productId, 2);
        shop.add(userId, productId, 3);
        CartSummary_24162111 cart = shop.cart(userId);
        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItemCount());
        assertEquals(new BigDecimal("5002.50"), cart.getTotal());
        shop.update(userId, cart.getItems().get(0).getId(), 4);
        assertEquals(new BigDecimal("4002.00"), shop.cart(userId).getTotal());
        shop.remove(userId, cart.getItems().get(0).getId());
        assertTrue(shop.cart(userId).getItems().isEmpty());
        shop.add(userId, productId, 1);
        shop.clear(userId);
        assertTrue(shop.cart(userId).getItems().isEmpty());
        assertEquals(10, stock());
    }

    @Test
    void quantityAndStockLimitsAreEnforced() {
        for (String invalid :
                new String[] {"0", "-1", "100", "1.5", "abc", "2147483648", "", null}) {
            assertThrows(
                    ShopException_24162111.class,
                    () -> CheckoutRules_24162111.parseQuantity(invalid));
        }
        shop.add(userId, productId, 10);
        assertThrows(ShopException_24162111.class, () -> shop.add(userId, productId, 1));
        assertThrows(
                ShopException_24162111.class,
                () -> shop.update(userId, shop.cart(userId).getItems().get(0).getId(), 11));
        changeProduct(p -> p.setStock(1000));
        assertThrows(ShopException_24162111.class, () -> shop.add(otherUserId, productId, 100));
        assertEquals(10, shop.cart(userId).getItemCount());
    }

    @Test
    void checkoutSnapshotsPriceDecreasesStockAndIsIdempotent() {
        shop.add(userId, productId, 4);
        CartSummary_24162111 cart = shop.cart(userId);
        String orderId = checkout(cart);
        assertEquals(orderId, checkout(cart));
        assertEquals(6, stock());
        assertTrue(shop.cart(userId).getItems().isEmpty());
        Cart_24162111 order = shop.order(userId, orderId);
        assertEquals(0, order.getStatus());
        assertEquals("COD", order.getPaymentMethod());
        assertEquals(new BigDecimal("4002.00"), order.getTotalAmount());
        changeProduct(
                p -> {
                    p.setPrice(99999);
                    p.setName("Tên đã sửa");
                });
        CartItem_24162111 item = shop.orderItems(userId, orderId).get(0);
        assertEquals(new BigDecimal("1000.50"), item.getUnitPrice());
        assertEquals("Sản phẩm kiểm thử", item.getProductName());
        assertEquals(1, shop.orders(userId, null, 1).size());
    }

    @Test
    void stalePriceRequiresReviewWithoutCreatingOrder() {
        shop.add(userId, productId, 2);
        CartSummary_24162111 cart = shop.cart(userId);
        changeProduct(p -> p.setPrice(2000.25));
        assertThrows(ShopException_24162111.class, () -> checkout(cart));
        assertEquals(10, stock());
        assertEquals(-1, shop.cart(userId).getCart().getStatus());
        assertTrue(shop.orders(userId, null, 1).isEmpty());
        checkout(shop.cart(userId));
        assertEquals(8, stock());
    }

    @Test
    void soldOutOrDisabledProductBlocksCheckout() {
        shop.add(userId, productId, 2);
        CartSummary_24162111 cart = shop.cart(userId);
        changeProduct(p -> p.setStock(1));
        assertThrows(ShopException_24162111.class, () -> checkout(cart));
        assertFalse(shop.cart(userId).isReady());
        changeProduct(
                p -> {
                    p.setStock(10);
                    p.setStatus(0);
                });
        assertThrows(ShopException_24162111.class, () -> checkout(cart));
        assertThrows(ShopException_24162111.class, () -> shop.add(otherUserId, productId, 1));
        assertEquals(10, stock());
        assertTrue(shop.orders(userId, null, 1).isEmpty());
    }

    @Test
    void ownershipAndAccountStatusAreChecked() {
        shop.add(userId, productId, 1);
        CartSummary_24162111 cart = shop.cart(userId);
        String itemId = cart.getItems().get(0).getId();
        assertThrows(ShopException_24162111.class, () -> shop.remove(otherUserId, itemId));
        assertThrows(ShopException_24162111.class, () -> shop.update(otherUserId, itemId, 1));
        assertThrows(
                ShopException_24162111.class,
                () ->
                        shop.checkout(
                                otherUserId,
                                cart.getCart().getId(),
                                cart.getFingerprint(),
                                shipping()));
        String orderId = checkout(cart);
        assertNull(shop.order(otherUserId, orderId));
        assertThrows(ShopException_24162111.class, () -> shop.orderItems(otherUserId, orderId));
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.find(User_24162111.class, userId).setStatus(0);
        em.getTransaction().commit();
        em.close();
        assertThrows(ShopException_24162111.class, () -> shop.cart(userId));
    }

    @Test
    void emptyCartAndInvalidShippingAreRejected() {
        CartSummary_24162111 cart = shop.cart(userId);
        assertThrows(ShopException_24162111.class, () -> checkout(cart));
        shop.add(userId, productId, 1);
        CartSummary_24162111 nonempty = shop.cart(userId);
        assertThrows(
                ShopException_24162111.class,
                () ->
                        shop.checkout(
                                userId,
                                nonempty.getCart().getId(),
                                nonempty.getFingerprint(),
                                new ShippingInfo_24162111("A", "123", "abc", "")));
        assertEquals(10, stock());
        new ShippingInfo_24162111("Người nhận", "+84901234567", "123 Đường A, TP HCM", "")
                .validate();
    }

    @Test
    void allEightStatusesReflectDatabaseUpdatesAndFilterCorrectly() {
        shop.add(userId, productId, 1);
        String orderId = checkout(shop.cart(userId));
        for (OrderStatus_24162111 status : OrderStatus_24162111.values()) {
            EntityManager em = factory.createEntityManager();
            em.getTransaction().begin();
            em.find(Cart_24162111.class, orderId).setStatus(status.getCode());
            em.getTransaction().commit();
            em.close();
            assertEquals(status.getLabel(), shop.order(userId, orderId).getStatusLabel());
            assertEquals(1, shop.orders(userId, status.getCode(), 1).size());
            assertEquals(1L, shop.statusCounts(userId).get(status.getCode()));
            assertTrue(shop.orders(userId, (status.getCode() + 1) % 8, 1).isEmpty());
        }
        assertThrows(IllegalArgumentException.class, () -> shop.orders(userId, 99, 1));
        assertTrue(shop.orders(otherUserId, null, 1).isEmpty());
    }

    @Test
    void secondProductFailureRollsBackEntireCheckout() {
        int secondId;
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Product_24162111 first = em.find(Product_24162111.class, productId);
        Product_24162111 second = new Product_24162111();
        second.setName("Sản phẩm 2");
        second.setSeller(first.getSeller());
        second.setCategory(first.getCategory());
        second.setPrice(2000);
        second.setStock(5);
        em.persist(second);
        em.getTransaction().commit();
        secondId = second.getId();
        em.close();
        shop.add(userId, productId, 2);
        shop.add(userId, secondId, 2);
        CartSummary_24162111 cart = shop.cart(userId);
        em = factory.createEntityManager();
        em.getTransaction().begin();
        em.find(Product_24162111.class, secondId).setStock(1);
        em.getTransaction().commit();
        em.close();
        assertThrows(ShopException_24162111.class, () -> checkout(cart));
        assertEquals(10, stock());
        assertEquals(2, shop.cart(userId).getItems().size());
        assertTrue(shop.orders(userId, null, 1).isEmpty());
    }

    @Test
    void concurrentBuyersCannotOversellLastUnit() throws Exception {
        changeProduct(p -> p.setStock(1));
        shop.add(userId, productId, 1);
        shop.add(otherUserId, productId, 1);
        CartSummary_24162111 first = shop.cart(userId);
        CartSummary_24162111 second = shop.cart(otherUserId);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<Boolean> a =
                    pool.submit(
                            () -> {
                                start.await();
                                return tryCheckout(userId, first);
                            });
            Future<Boolean> b =
                    pool.submit(
                            () -> {
                                start.await();
                                return tryCheckout(otherUserId, second);
                            });
            start.countDown();
            int successes =
                    (a.get(20, TimeUnit.SECONDS) ? 1 : 0) + (b.get(20, TimeUnit.SECONDS) ? 1 : 0);
            assertEquals(1, successes);
            assertEquals(0, stock());
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void orderHistoryPaginatesSixOrdersPerPage() {
        String latestId = null;
        for (int i = 0; i < 7; i++) {
            shop.add(userId, productId, 1);
            latestId = checkout(shop.cart(userId));
        }
        assertEquals(6, shop.orders(userId, null, 1).size());
        assertEquals(1, shop.orders(userId, null, 2).size());
        assertEquals(latestId, shop.orders(userId, 0, 1).get(0).getId());
        assertEquals(7L, shop.statusCounts(userId).get(0));
        assertTrue(shop.orders(userId, 1, 1).isEmpty());
    }

    @Test
    void concurrentResubmissionsCreateOneOrderAndDeductStockOnce() throws Exception {
        shop.add(userId, productId, 2);
        CartSummary_24162111 cart = shop.cart(userId);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<String> a =
                    pool.submit(
                            () -> {
                                start.await();
                                return checkout(cart);
                            });
            Future<String> b =
                    pool.submit(
                            () -> {
                                start.await();
                                return checkout(cart);
                            });
            start.countDown();
            assertEquals(a.get(20, TimeUnit.SECONDS), b.get(20, TimeUnit.SECONDS));
            assertEquals(8, stock());
            assertEquals(1, shop.orders(userId, null, 1).size());
        } finally {
            pool.shutdownNow();
        }
    }

    private boolean tryCheckout(int owner, CartSummary_24162111 cart) {
        try {
            shop.checkout(owner, cart.getCart().getId(), cart.getFingerprint(), shipping());
            return true;
        } catch (ShopException_24162111 expected) {
            return false;
        }
    }
}
