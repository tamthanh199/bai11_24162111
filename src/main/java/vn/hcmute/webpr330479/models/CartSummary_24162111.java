package vn.hcmute.webpr330479.models;

import vn.hcmute.webpr330479.services.CheckoutRules_24162111;

import java.math.BigDecimal;
import java.util.List;

public class CartSummary_24162111 {
    private final Cart_24162111 cart;
    private final List<CartItem_24162111> items;
    private final BigDecimal total;
    private final String fingerprint;
    private final boolean ready;

    public CartSummary_24162111(Cart_24162111 cart, List<CartItem_24162111> items) {
        this.cart = cart;
        this.items = List.copyOf(items);
        this.total =
                items.stream()
                        .map(CartItem_24162111::getLineTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.fingerprint = CheckoutRules_24162111.fingerprint(items);
        this.ready =
                !items.isEmpty()
                        && items.stream()
                                .allMatch(
                                        item ->
                                                CheckoutRules_24162111.isAvailable(
                                                                item.getProduct())
                                                        && item.getQuantity() >= 1
                                                        && item.getQuantity()
                                                                <= CheckoutRules_24162111.limit(
                                                                        item.getProduct()));
    }

    public Cart_24162111 getCart() {
        return cart;
    }

    public List<CartItem_24162111> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public boolean isReady() {
        return ready;
    }

    public int getItemCount() {
        return items.stream().mapToInt(CartItem_24162111::getQuantity).sum();
    }
}
