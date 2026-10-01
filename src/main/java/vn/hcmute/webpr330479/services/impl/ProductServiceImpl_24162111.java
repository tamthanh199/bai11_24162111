package vn.hcmute.webpr330479.services.impl;

import vn.hcmute.webpr330479.dao.ProductDao_24162111;
import vn.hcmute.webpr330479.dao.impl.ProductDaoImpl_24162111;
import vn.hcmute.webpr330479.models.Product_24162111;
import vn.hcmute.webpr330479.services.ProductService_24162111;

import java.util.List;

public class ProductServiceImpl_24162111 implements ProductService_24162111 {
    private final ProductDao_24162111 dao = new ProductDaoImpl_24162111();

    public List<Product_24162111> findBySellerId(int id) {
        return dao.findBySellerId(id);
    }

    public List<Product_24162111> findPage(int p, int s) {
        return dao.findPage(p, s);
    }

    public long count() {
        return dao.count();
    }

    public Product_24162111 findById(int id) {
        return dao.findById(id);
    }

    public void save(Product_24162111 p) {
        if (p.getId() > 0) {
            dao.update(p);
        } else {
            dao.insert(p);
        }
    }

    public void delete(int id) {
        dao.delete(id);
    }
}
