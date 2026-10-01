package vn.hcmute.webpr330479.services;

import vn.hcmute.webpr330479.models.CartItem_24162111;
import vn.hcmute.webpr330479.models.Product_24162111;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;

public final class CheckoutRules_24162111 {
    public static final int MAX_QUANTITY = 99;

    private CheckoutRules_24162111() {}

    public static boolean isAvailable(Product_24162111 product) {
        return product != null
                && product.getStatus() == 1
                && product.getStock() > 0
                && product.getCategory() != null
                && product.getCategory().getStatus() == 1
                && product.getSeller() != null
                && product.getSeller().getStatus() == 1
                && Double.isFinite(product.getPrice())
                && product.getPrice() >= 0;
    }

    public static int limit(Product_24162111 product) {
        return isAvailable(product) ? Math.min(MAX_QUANTITY, product.getStock()) : 0;
    }

    public static int parseQuantity(String value) {
        try {
            int quantity = Integer.parseInt(value);
            if (quantity >= 1 && quantity <= MAX_QUANTITY) {
                return quantity;
            }
        } catch (NumberFormatException ignored) {

        }
        throw new ShopException_24162111(
                "Số lượng phải là số nguyên từ 1 đến " + MAX_QUANTITY + ".");
    }

    public static void requireQuantity(Product_24162111 product, int quantity) {
        if (!isAvailable(product)) {
            throw new ShopException_24162111(
                    "Sản phẩm đã hết hàng hoặc ngừng bán. Hãy xóa khỏi giỏ hàng.");
        }
        if (quantity < 1 || quantity > limit(product)) {
            throw new ShopException_24162111(
                    "Số lượng của " + product.getName() + " phải từ 1 đến " + limit(product) + ".");
        }
    }

    public static BigDecimal price(Product_24162111 product) {
        double price = product.getPrice();
        if (!Double.isFinite(price) || price < 0 || price > 999999999999999.0) {
            throw new ShopException_24162111(
                    "Giá sản phẩm không hợp lệ. Vui lòng liên hệ cửa hàng.");
        }
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }

    public static String fingerprint(List<CartItem_24162111> items) {
        StringBuilder data = new StringBuilder();
        items.stream()
                .sorted(Comparator.comparingInt(item -> item.getProduct().getId()))
                .forEach(
                        item ->
                                data.append(item.getProduct().getId())
                                        .append(':')
                                        .append(item.getQuantity())
                                        .append(':')
                                        .append(item.getUnitPrice().toPlainString())
                                        .append(';'));
        try {
            byte[] hash =
                    MessageDigest.getInstance("SHA-256")
                            .digest(data.toString().getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is required by Java.", e);
        }
    }
}
