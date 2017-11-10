package com.cxf.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.cxf.enums.UserSexEnum;

/**
 * 
 * @Date: 2017年10月23日 下午1:21:36
 * @author chenxf
 */
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String password;
    private UserSexEnum usersex;
    private String nickname;

    private Date lastPasswordResetDate;

    private Set<String> roles = new HashSet<>();

    public UserEntity() {
        super();
    }

    public UserEntity(String userName, String passWord, UserSexEnum userSex) {
        super();
        this.password = passWord;
        this.username = userName;
        this.usersex = userSex;

    }

    public void addRole(String roleName) {
        roles.add(roleName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserSexEnum getUsersex() {
        return usersex;
    }

    public void setUsersex(UserSexEnum usersex) {
        this.usersex = usersex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "userName " + this.username + ", pasword " + this.password + "sex " + usersex.name();
    }

}
