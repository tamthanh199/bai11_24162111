package vn.hcmute.webpr330479.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "Cart", schema = "dbo")
public class Cart_24162111 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cartId", columnDefinition = "NVARCHAR(50)")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User_24162111 user;

    @Column(name = "buyDate")
    private LocalDateTime buyDate;

    @Column(name = "status")
    private int status = OrderStatus_24162111.ACTIVE_CART;

    @Column(name = "recipientName", length = 100)
    private String recipientName;

    @Column(name = "recipientPhone", length = 20)
    private String recipientPhone;

    @Column(name = "shippingAddress", length = 500)
    private String shippingAddress;

    @Column(name = "orderNote", length = 500)
    private String orderNote;

    @Column(name = "paymentMethod", length = 10)
    private String paymentMethod;

    @Column(name = "totalAmount", precision = 19, scale = 2)
    private BigDecimal totalAmount;

    public Cart_24162111() {}

    @PrePersist
    public void prePersist() {
        if (id == null || id.isBlank()) {
            id = "CART-" + UUID.randomUUID().toString().toUpperCase();
        }
        if (buyDate == null) {
            buyDate = LocalDateTime.now();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User_24162111 getUser() {
        return user;
    }

    public void setUser(User_24162111 user) {
        this.user = user;
    }

    public LocalDateTime getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(LocalDateTime buyDate) {
        this.buyDate = buyDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Transient
    public String getStatusLabel() {
        return status == OrderStatus_24162111.ACTIVE_CART
                ? "Giỏ hàng"
                : OrderStatus_24162111.fromCode(status).getLabel();
    }

    @Transient
    public String getFormattedBuyDate() {
        return buyDate == null
                ? ""
                : buyDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
