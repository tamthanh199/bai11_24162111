package vn.hcmute.webpr330479.dao;

import vn.hcmute.webpr330479.models.UserRole_24162111;
import vn.hcmute.webpr330479.models.User_24162111;

public interface UserDao_24162111 {
    User_24162111 login(String login, String password);

    boolean existsUsername(String username);

    boolean existsEmail(String email);

    UserRole_24162111 findRoleByName(String name);

    void insert(User_24162111 user);
}
