package vn.hcmute.webpr330479.dao.impl;

import jakarta.persistence.*;

import vn.hcmute.webpr330479.config.JPAConfig_24162111;
import vn.hcmute.webpr330479.dao.CategoryDao_24162111;
import vn.hcmute.webpr330479.models.Category_24162111;

import java.util.List;

public class CategoryDaoImpl_24162111 implements CategoryDao_24162111 {
    public List<Category_24162111> findAll() {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Category_24162111 c ORDER BY c.id",
                            Category_24162111.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Category_24162111> findPage(int page, int size) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Category_24162111 c ORDER BY c.id DESC",
                            Category_24162111.class)
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
            return em.createQuery("SELECT COUNT(c) FROM Category_24162111 c", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public Category_24162111 findById(int id) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.find(Category_24162111.class, id);
        } finally {
            em.close();
        }
    }

    public void insert(Category_24162111 c) {
        save(c, false);
    }

    public void update(Category_24162111 c) {
        save(c, true);
    }

    private void save(Category_24162111 c, boolean merge) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (merge) {
                em.merge(c);
            } else {
                em.persist(c);
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
            Category_24162111 c = em.find(Category_24162111.class, id);
            if (c != null) {
                em.remove(c);
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
