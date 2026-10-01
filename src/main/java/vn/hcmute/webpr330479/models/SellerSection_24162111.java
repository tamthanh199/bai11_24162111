package vn.hcmute.webpr330479.models;

import java.util.List;

public class SellerSection_24162111 {
    private Seller_24162111 seller;
    private List<Product_24162111> products;

    public SellerSection_24162111(Seller_24162111 seller, List<Product_24162111> products) {
        this.seller = seller;
        this.products = products;
    }

    public Seller_24162111 getSeller() {
        return seller;
    }

    public List<Product_24162111> getProducts() {
        return products;
    }
}
