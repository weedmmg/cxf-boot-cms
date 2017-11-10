package com.cxf.auth;

import lombok.Data;

import com.cxf.entity.UserEntity;

@Data
class Auth {

    public Auth(String token, UserEntity user) {
        this.token = token;
        this.user = user;
    }

    private String token;
    private UserEntity user;
}
