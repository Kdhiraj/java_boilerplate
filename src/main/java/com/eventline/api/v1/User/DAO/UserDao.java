package com.eventline.api.v1.User.DAO;

import com.eventline.api.v1.User.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByEmail(String email);
    User save(User user);
    Optional<User> findByUserId(String userId);
}
