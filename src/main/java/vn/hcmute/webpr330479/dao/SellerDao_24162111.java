package vn.hcmute.webpr330479.dao;

import vn.hcmute.webpr330479.models.Seller_24162111;

import java.util.List;

public interface SellerDao_24162111 {
    List<Seller_24162111> findAllActive();

    Seller_24162111 findById(int id);
}
