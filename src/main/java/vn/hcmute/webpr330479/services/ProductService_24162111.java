package vn.hcmute.webpr330479.services;

import vn.hcmute.webpr330479.models.Product_24162111;

import java.util.List;

public interface ProductService_24162111 {
    List<Product_24162111> findBySellerId(int sellerId);

    List<Product_24162111> findPage(int page, int size);

    long count();

    Product_24162111 findById(int id);

    void save(Product_24162111 p);

    void delete(int id);
}
