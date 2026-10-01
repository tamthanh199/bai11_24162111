package vn.hcmute.webpr330479.services;

import vn.hcmute.webpr330479.models.User_24162111;

public interface UserService_24162111 {
    User_24162111 login(String login, String password);

    boolean existsUsername(String username);

    boolean existsEmail(String email);

    void register(User_24162111 user);
}
