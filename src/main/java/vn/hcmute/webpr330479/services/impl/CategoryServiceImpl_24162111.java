package vn.hcmute.webpr330479.services.impl;

import vn.hcmute.webpr330479.dao.CategoryDao_24162111;
import vn.hcmute.webpr330479.dao.impl.CategoryDaoImpl_24162111;
import vn.hcmute.webpr330479.models.Category_24162111;
import vn.hcmute.webpr330479.services.CategoryService_24162111;

import java.util.List;

public class CategoryServiceImpl_24162111 implements CategoryService_24162111 {
    private final CategoryDao_24162111 dao = new CategoryDaoImpl_24162111();

    public List<Category_24162111> findAll() {
        return dao.findAll();
    }

    public List<Category_24162111> findPage(int p, int s) {
        return dao.findPage(p, s);
    }

    public long count() {
        return dao.count();
    }

    public Category_24162111 findById(int id) {
        return dao.findById(id);
    }

    public void save(Category_24162111 c) {
        if (c.getId() > 0) {
            dao.update(c);
        } else {
            dao.insert(c);
        }
    }

    public void delete(int id) {
        dao.delete(id);
    }
}
