package vn.hcmute.webpr330479.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "Product", schema = "dbo")
public class Product_24162111 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private int id;

    @Column(name = "productName", columnDefinition = "NVARCHAR(200)")
    private String name;

    @Column(name = "productCode")
    private Long code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId")
    private Category_24162111 category;

    @Column(name = "description", columnDefinition = "NVARCHAR(500)")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "amount")
    private int amount;

    @Column(name = "stock")
    private int stock;

    @Column(name = "images", columnDefinition = "NVARCHAR(500)")
    private String images;

    @Column(name = "wishlist")
    private int wishlist;

    @Column(name = "status")
    private int status = 1;

    @Column(name = "createDate")
    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sellerId")
    private Seller_24162111 seller;

    public Product_24162111() {}

    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = LocalDate.now();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Category_24162111 getCategory() {
        return category;
    }

    public void setCategory(Category_24162111 category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getWishlist() {
        return wishlist;
    }

    public void setWishlist(int wishlist) {
        this.wishlist = wishlist;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public Seller_24162111 getSeller() {
        return seller;
    }

    public void setSeller(Seller_24162111 seller) {
        this.seller = seller;
    }

    @Transient
    public int getPurchaseLimit() {
        return vn.hcmute.webpr330479.services.CheckoutRules_24162111.limit(this);
    }

    @Transient
    public boolean isPurchasable() {
        return getPurchaseLimit() > 0;
    }
}
