package vn.hcmute.webpr330479.dao;

import vn.hcmute.webpr330479.models.Product_24162111;

import java.util.List;

public interface ProductDao_24162111 {
    List<Product_24162111> findBySellerId(int sellerId);

    List<Product_24162111> findPage(int page, int size);

    long count();

    Product_24162111 findById(int id);

    void insert(Product_24162111 p);

    void update(Product_24162111 p);

    void delete(int id);
}
