package vn.hcmute.webpr330479.services.impl;

import vn.hcmute.webpr330479.dao.UserDao_24162111;
import vn.hcmute.webpr330479.dao.impl.UserDaoImpl_24162111;
import vn.hcmute.webpr330479.models.UserRole_24162111;
import vn.hcmute.webpr330479.models.User_24162111;
import vn.hcmute.webpr330479.services.UserService_24162111;

public class UserServiceImpl_24162111 implements UserService_24162111 {
    private final UserDao_24162111 dao = new UserDaoImpl_24162111();

    public User_24162111 login(String login, String password) {
        if (login == null || password == null) {
            return null;
        }
        return dao.login(login.trim(), password);
    }

    public boolean existsUsername(String u) {
        return dao.existsUsername(u);
    }

    public boolean existsEmail(String e) {
        return dao.existsEmail(e);
    }

    public void register(User_24162111 user) {
        UserRole_24162111 role = dao.findRoleByName("USER");
        if (role == null) {
            throw new IllegalStateException("Chưa có role USER trong database.");
        }
        user.setRole(role);
        user.setSeller(null);
        user.setStatus(1);
        user.setCode(null);
        dao.insert(user);
    }
}
