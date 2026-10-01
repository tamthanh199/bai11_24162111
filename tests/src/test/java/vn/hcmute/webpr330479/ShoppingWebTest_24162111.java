package vn.hcmute.webpr330479;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.junit.jupiter.api.*;

import vn.hcmute.webpr330479.config.JPAConfig_24162111;
import vn.hcmute.webpr330479.controllers.*;
import vn.hcmute.webpr330479.filters.ShopSecurityFilter_24162111;
import vn.hcmute.webpr330479.models.*;

import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

class ShoppingWebTest_24162111 {
    private static Tomcat tomcat;
    private static String base;
    private static int productId;
    private HttpClient browser;

    @BeforeAll
    static void start() throws Exception {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        em.getTransaction().begin();
        UserRole_24162111 role = new UserRole_24162111();
        role.setRoleName("USER");
        em.persist(role);
        User_24162111 user = new User_24162111();
        user.setUsername("webuser");
        user.setPassword("123456");
        user.setFullName("Nguyễn Văn A");
        user.setPhone("0901234567");
        user.setRole(role);
        em.persist(user);
        Category_24162111 category = new Category_24162111();
        category.setName("Danh mục demo");
        em.persist(category);
        Seller_24162111 seller = new Seller_24162111();
        seller.setName("Seller demo");
        seller.setStatus(1);
        em.persist(seller);
        Product_24162111 product = new Product_24162111();
        product.setName("Sản phẩm <demo>");
        product.setCode(123L);
        product.setPrice(10000);
        product.setAmount(10);
        product.setStock(10);
        product.setSeller(seller);
        product.setCategory(category);
        product.setImages("assets/no-product.svg");
        em.persist(product);
        em.getTransaction().commit();
        productId = product.getId();
        em.close();

        tomcat = new Tomcat();
        tomcat.setBaseDir(Files.createTempDirectory("shopping-tomcat").toString());
        tomcat.setPort(0);
        tomcat.getConnector().setProperty("address", "127.0.0.1");
        Path webapp = Path.of("..", "src", "main", "webapp").toAbsolutePath().normalize();
        Context context = tomcat.addWebapp("/shop", webapp.toString());
        context.setParentClassLoader(ShoppingWebTest_24162111.class.getClassLoader());

        servlet(context, "login", new LoginController_24162111(), "/login");
        servlet(context, "home", new HomeController_24162111(), "/home", "/products");
        servlet(context, "product", new ProductDetailController_24162111(), "/product");
        servlet(context, "media", new MediaController_24162111(), "/media");
        servlet(
                context,
                "cart",
                new CartController_24162111(),
                "/cart",
                "/cart/add",
                "/cart/update",
                "/cart/remove",
                "/cart/clear");
        servlet(context, "checkout", new CheckoutController_24162111(), "/checkout");
        servlet(context, "orders", new OrderController_24162111(), "/orders", "/orders/detail");
        FilterDef security = new FilterDef();
        security.setFilterName("shop-security");
        security.setFilterClass(ShopSecurityFilter_24162111.class.getName());
        security.setFilter(new ShopSecurityFilter_24162111());
        context.addFilterDef(security);
        FilterMap mapping = new FilterMap();
        mapping.setFilterName("shop-security");
        mapping.addURLPattern("/*");
        context.addFilterMapBefore(mapping);
        tomcat.start();
        base = "http://127.0.0.1:" + tomcat.getConnector().getLocalPort() + "/shop";
    }

    private static void servlet(
            Context context,
            String name,
            jakarta.servlet.http.HttpServlet servlet,
            String... routes) {
        Tomcat.addServlet(context, name, servlet);
        for (String route : routes) {
            context.addServletMappingDecoded(route, name);
        }
    }

    @AfterAll
    static void stop() throws Exception {
        if (tomcat != null) {
            tomcat.stop();
            tomcat.destroy();
        }
        JPAConfig_24162111.close();
    }

    @BeforeEach
    void browser() {
        browser =
                HttpClient.newBuilder()
                        .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                        .followRedirects(HttpClient.Redirect.NEVER)
                        .build();
    }

    private HttpResponse<String> get(String path) throws Exception {
        return browser.send(
                HttpRequest.newBuilder(URI.create(base + path)).GET().build(),
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private HttpResponse<String> post(String path, Map<String, String> values) throws Exception {
        StringJoiner body = new StringJoiner("&");
        values.forEach(
                (key, value) ->
                        body.add(
                                URLEncoder.encode(key, StandardCharsets.UTF_8)
                                        + "="
                                        + URLEncoder.encode(value, StandardCharsets.UTF_8)));
        return browser.send(
                HttpRequest.newBuilder(URI.create(base + path))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                        .build(),
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private String hidden(String html, String name) {
        Matcher matcher = Pattern.compile("name=\"" + name + "\" value=\"([^\"]*)\"").matcher(html);
        assertTrue(matcher.find(), "Missing hidden field: " + name + ": " + html);
        return matcher.group(1);
    }

    private String login() throws Exception {
        assertEquals(200, get("/login").statusCode());
        assertEquals(
                302, post("/login", Map.of("login", "webuser", "password", "123456")).statusCode());
        HttpResponse<String> home = get("/home");
        assertEquals(200, home.statusCode(), home.body());
        assertTrue(home.body().contains("Sản phẩm &lt;demo&gt;"));
        assertTrue(
                home.body().contains("Thêm vào giỏ"),
                "UTF-8 include fragment must render correctly");
        assertTrue(home.body().contains("DE05"));
        assertFalse(home.body().contains("<sitemesh:write"));
        return hidden(home.body(), "csrfToken");
    }

    @Test
    void anonymousCartRedirectsToLogin() throws Exception {
        HttpResponse<String> response = get("/cart");
        assertEquals(302, response.statusCode());
        assertEquals("/shop/login", response.headers().firstValue("Location").orElseThrow());
    }

    @Test
    void postWithoutCsrfIsRejectedAndGetCannotMutateCart() throws Exception {
        login();
        assertEquals(
                403,
                post("/cart/add", Map.of("productId", String.valueOf(productId), "quantity", "1"))
                        .statusCode());
        assertEquals(405, get("/cart/add?productId=" + productId + "&quantity=1").statusCode());
    }

    @Test
    void completeCodFlowRendersAllEightFiltersAndEscapesShipping() throws Exception {
        String csrf = login();
        assertEquals(302, post("/cart/clear", Map.of("csrfToken", csrf)).statusCode());
        assertEquals(
                302,
                post(
                                "/cart/add",
                                Map.of(
                                        "csrfToken",
                                        csrf,
                                        "productId",
                                        String.valueOf(productId),
                                        "quantity",
                                        "2"))
                        .statusCode());
        HttpResponse<String> cart = get("/cart");
        assertEquals(200, cart.statusCode(), cart.body());
        assertTrue(cart.body().contains("Thanh toán COD"));
        HttpResponse<String> checkout = get("/checkout");
        assertEquals(200, checkout.statusCode(), checkout.body());
        Map<String, String> order =
                Map.of(
                        "csrfToken",
                        csrf,
                        "cartId",
                        hidden(checkout.body(), "cartId"),
                        "fingerprint",
                        hidden(checkout.body(), "fingerprint"),
                        "recipientName",
                        "Nguyễn Văn A",
                        "recipientPhone",
                        "0901234567",
                        "shippingAddress",
                        "123 Đường A, Thành phố Hồ Chí Minh",
                        "orderNote",
                        "<script>alert(1)</script>");
        HttpResponse<String> placed = post("/checkout", order);
        assertEquals(302, placed.statusCode(), placed.body());
        String location = placed.headers().firstValue("Location").orElseThrow();
        HttpResponse<String> detail = get(location.substring("/shop".length()));
        assertEquals(200, detail.statusCode(), detail.body());
        assertTrue(detail.body().contains("Đặt hàng COD thành công"));
        assertTrue(detail.body().contains("&lt;script&gt;alert(1)&lt;/script&gt;"));
        assertFalse(detail.body().contains("<script>alert(1)</script>"));
        assertEquals(
                location, post("/checkout", order).headers().firstValue("Location").orElseThrow());
        HttpResponse<String> history = get("/orders");
        assertEquals(200, history.statusCode(), history.body());
        for (OrderStatus_24162111 status : OrderStatus_24162111.values()) {
            assertTrue(history.body().contains(status.getLabel()), status.getLabel());
            assertEquals(200, get("/orders?status=" + status.getCode()).statusCode());
        }
        assertEquals(400, get("/orders?status=99").statusCode());
        assertEquals(404, get("/orders/detail?id=CART-NOT-OWNED").statusCode());
    }
}
