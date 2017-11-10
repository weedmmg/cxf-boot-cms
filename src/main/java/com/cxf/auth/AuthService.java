package com.cxf.auth;

import com.cxf.entity.UserEntity;

public interface AuthService {

    UserEntity register(UserEntity userToAdd);

    String login(String username, String password);

    String refresh(String oldToken);
}
