package com.ymc.iotthings.model;


import javax.persistence.Column;
import javax.persistence.Id;

/**
 * user info table bean
 * 参考博客：https://juejin.im/post/5ad6b3c3f265da237c696ba0
 * package name: com.vip.things.model
 * date :2019年3月25日 08:54:10
 * author : ymc
 **/

public class UserInfo {

    /**
     * primary key
     */
    @Id
    private String id;

    /**
     * username
     */
    @Column(name = "user_name")
    private String userName;

    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
