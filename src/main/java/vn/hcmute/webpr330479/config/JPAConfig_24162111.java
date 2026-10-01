package vn.hcmute.webpr330479.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public final class JPAConfig_24162111 {
    private static final EntityManagerFactory FACTORY = createFactory();

    private JPAConfig_24162111() {}

    private static EntityManagerFactory createFactory() {
        Map<String, Object> properties = new HashMap<>();
        putIfPresent(properties, "jakarta.persistence.jdbc.url", "DB_URL");
        putIfPresent(properties, "jakarta.persistence.jdbc.user", "DB_USERNAME");
        putIfPresent(properties, "jakarta.persistence.jdbc.password", "DB_PASSWORD");
        return Persistence.createEntityManagerFactory("de05-jpa", properties);
    }

    private static void putIfPresent(Map<String, Object> properties, String jpaKey, String envKey) {
        String value = System.getenv(envKey);
        if (value == null || value.isBlank()) {
            value = System.getProperty(envKey);
        }
        if (value != null && !value.isBlank()) {
            properties.put(jpaKey, value.trim());
        }
    }

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void close() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
