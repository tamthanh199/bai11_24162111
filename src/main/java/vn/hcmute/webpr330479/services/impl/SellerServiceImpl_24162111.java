package vn.hcmute.webpr330479.services.impl;

import vn.hcmute.webpr330479.dao.SellerDao_24162111;
import vn.hcmute.webpr330479.dao.impl.SellerDaoImpl_24162111;
import vn.hcmute.webpr330479.models.Seller_24162111;
import vn.hcmute.webpr330479.services.SellerService_24162111;

import java.util.List;

public class SellerServiceImpl_24162111 implements SellerService_24162111 {
    private final SellerDao_24162111 dao = new SellerDaoImpl_24162111();

    public List<Seller_24162111> findAllActive() {
        return dao.findAllActive();
    }

    public Seller_24162111 findById(int id) {
        return dao.findById(id);
    }
}
