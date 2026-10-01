package vn.hcmute.webpr330479.services;

import vn.hcmute.webpr330479.models.Category_24162111;

import java.util.List;

public interface CategoryService_24162111 {
    List<Category_24162111> findAll();

    List<Category_24162111> findPage(int page, int size);

    long count();

    Category_24162111 findById(int id);

    void save(Category_24162111 c);

    void delete(int id);
}
