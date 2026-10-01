package vn.hcmute.webpr330479.dao.impl;

import jakarta.persistence.EntityManager;

import vn.hcmute.webpr330479.config.JPAConfig_24162111;
import vn.hcmute.webpr330479.dao.SellerDao_24162111;
import vn.hcmute.webpr330479.models.Seller_24162111;

import java.util.List;

public class SellerDaoImpl_24162111 implements SellerDao_24162111 {
    public List<Seller_24162111> findAllActive() {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Seller_24162111 s WHERE s.status=1 ORDER BY s.id",
                            Seller_24162111.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Seller_24162111 findById(int id) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.find(Seller_24162111.class, id);
        } finally {
            em.close();
        }
    }
}
