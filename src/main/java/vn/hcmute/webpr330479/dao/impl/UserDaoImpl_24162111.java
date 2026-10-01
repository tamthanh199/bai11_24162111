package vn.hcmute.webpr330479.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import vn.hcmute.webpr330479.config.JPAConfig_24162111;
import vn.hcmute.webpr330479.dao.UserDao_24162111;
import vn.hcmute.webpr330479.models.UserRole_24162111;
import vn.hcmute.webpr330479.models.User_24162111;

public class UserDaoImpl_24162111 implements UserDao_24162111 {
    public User_24162111 login(String login, String password) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM User_24162111 u LEFT JOIN FETCH u.role LEFT JOIN FETCH"
                                + " u.seller WHERE (LOWER(u.username)=:login OR"
                                + " LOWER(u.email)=:login) AND u.password=:password AND u.status=1",
                            User_24162111.class)
                    .setParameter("login", login.toLowerCase())
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public boolean existsUsername(String username) {
        return count("SELECT COUNT(u) FROM User_24162111 u WHERE LOWER(u.username)=:v", username)
                > 0;
    }

    public boolean existsEmail(String email) {
        return count("SELECT COUNT(u) FROM User_24162111 u WHERE LOWER(u.email)=:v", email) > 0;
    }

    private long count(String jpql, String v) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.createQuery(jpql, Long.class)
                    .setParameter("v", v.toLowerCase())
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public UserRole_24162111 findRoleByName(String name) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM UserRole_24162111 r WHERE UPPER(r.roleName)=:n",
                            UserRole_24162111.class)
                    .setParameter("n", name.toUpperCase())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void insert(User_24162111 user) {
        EntityManager em = JPAConfig_24162111.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (user.getRole() != null)
                user.setRole(em.getReference(UserRole_24162111.class, user.getRole().getId()));
            em.persist(user);
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
