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
     * 主键
     */
    @Id
    private String id;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

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

}
