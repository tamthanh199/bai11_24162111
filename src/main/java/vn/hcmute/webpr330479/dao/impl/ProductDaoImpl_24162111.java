package vn.hcmute.webpr330479.dao.impl;

import jakarta.persistence.*;

import vn.hcmute.webpr330479.config.JPAConfig_24162111;
import vn.hcmute.webpr330479.dao.ProductDao_24162111;
import vn.hcmute.webpr330479.models.Category_24162111;
import vn.hcmute.webpr330479.models.Product_24162111;
import vn.hcmute.webpr330479.models.Seller_24162111;

import java.util.List;

public class ProductDaoImpl_24162111 implements ProductDao_24162111 {
    public List<Product_24162111> findBySellerId(int sellerId) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Product_24162111 p JOIN FETCH p.category JOIN FETCH"
                                + " p.seller WHERE p.seller.id=:sid AND p.status=1 ORDER BY p.id"
                                + " DESC",
                            Product_24162111.class)
                    .setParameter("sid", sellerId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Product_24162111> findPage(int page, int size) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Product_24162111 p LEFT JOIN FETCH p.category LEFT JOIN"
                                    + " FETCH p.seller ORDER BY p.id DESC",
                            Product_24162111.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long count() {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Product_24162111 p", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public Product_24162111 findById(int id) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            try {
                return em.createQuery(
                                "SELECT p FROM Product_24162111 p LEFT JOIN FETCH p.category LEFT"
                                        + " JOIN FETCH p.seller WHERE p.id=:id",
                                Product_24162111.class)
                        .setParameter("id", id)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public void insert(Product_24162111 p) {
        save(p, false);
    }

    public void update(Product_24162111 p) {
        save(p, true);
    }

    private void save(Product_24162111 p, boolean merge) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (p.getCategory() != null)
                p.setCategory(em.getReference(Category_24162111.class, p.getCategory().getId()));
            if (p.getSeller() != null)
                p.setSeller(em.getReference(Seller_24162111.class, p.getSeller().getId()));
            if (merge) {
                em.merge(p);
            } else {
                em.persist(p);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Product_24162111 p = em.find(Product_24162111.class, id);
            if (p != null) {
                em.remove(p);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
