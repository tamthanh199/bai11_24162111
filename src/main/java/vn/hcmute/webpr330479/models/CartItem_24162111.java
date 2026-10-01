package vn.hcmute.webpr330479.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "CartItem", schema = "dbo")
public class CartItem_24162111 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cartItemId", columnDefinition = "NVARCHAR(50)")
    private String id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unitPrice", precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "productName", length = 200)
    private String productName;

    @Column(name = "productCode")
    private Long productCode;

    @Column(name = "productImage", length = 500)
    private String productImage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product_24162111 product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartId")
    private Cart_24162111 cart;

    public CartItem_24162111() {}

    @PrePersist
    public void prePersist() {
        if (id == null || id.isBlank()) {
            id = "ITEM-" + UUID.randomUUID().toString().toUpperCase();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Product_24162111 getProduct() {
        return product;
    }

    public void setProduct(Product_24162111 product) {
        this.product = product;
    }

    public Cart_24162111 getCart() {
        return cart;
    }

    public void setCart(Cart_24162111 cart) {
        this.cart = cart;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductCode() {
        return productCode;
    }

    public void setProductCode(Long productCode) {
        this.productCode = productCode;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @Transient
    public BigDecimal getLineTotal() {
        return unitPrice == null
                ? BigDecimal.ZERO
                : unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
