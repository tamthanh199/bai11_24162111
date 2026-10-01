package vn.hcmute.webpr330479.utils;

import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class FileUploadUtil_24162111 {
    private static final Path PRODUCT_DIR =
            Paths.get(System.getProperty("user.home"), "De05Uploads", "product");
    private static final Path CATEGORY_DIR =
            Paths.get(System.getProperty("user.home"), "De05Uploads", "category");

    private FileUploadUtil_24162111() {}

    public static String saveProductImage(Part p) throws IOException {
        return save(p, PRODUCT_DIR);
    }

    public static String saveCategoryImage(Part p) throws IOException {
        return save(p, CATEGORY_DIR);
    }

    private static String save(Part part, Path dir) throws IOException {
        if (part == null
                || part.getSize() == 0
                || part.getSubmittedFileName() == null
                || part.getSubmittedFileName().isBlank()) return null;
        String original = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        int dot = original.lastIndexOf('.');
        if (dot < 0) {
            throw new IOException("File ảnh phải có phần mở rộng.");
        }
        String ext = original.substring(dot).toLowerCase(Locale.ROOT);
        if (!List.of(".jpg", ".jpeg", ".png", ".gif", ".webp").contains(ext))
            throw new IOException("Chỉ chấp nhận JPG, JPEG, PNG, GIF hoặc WEBP.");
        Files.createDirectories(dir);
        String name = UUID.randomUUID() + ext;
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, dir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
        }
        return name;
    }

    public static Path getProductPath(String n) {
        return safe(PRODUCT_DIR, n);
    }

    public static Path getCategoryPath(String n) {
        return safe(CATEGORY_DIR, n);
    }

    private static Path safe(Path dir, String name) {
        Path base = dir.toAbsolutePath().normalize();
        Path p = base.resolve(name).normalize();
        if (!p.startsWith(base)) {
            throw new IllegalArgumentException("Tên file không hợp lệ.");
        }
        return p;
    }

    public static void deleteProductImage(String n) throws IOException {
        delete(PRODUCT_DIR, n);
    }

    public static void deleteCategoryImage(String n) throws IOException {
        delete(CATEGORY_DIR, n);
    }

    private static void delete(Path dir, String n) throws IOException {
        if (n == null || n.isBlank() || n.startsWith("assets/")) {
            return;
        }
        Files.deleteIfExists(safe(dir, n));
    }
}
