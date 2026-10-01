package vn.hcmute.webpr330479.services;

import vn.hcmute.webpr330479.models.Seller_24162111;

import java.util.List;

public interface SellerService_24162111 {
    List<Seller_24162111> findAllActive();

    Seller_24162111 findById(int id);
}
