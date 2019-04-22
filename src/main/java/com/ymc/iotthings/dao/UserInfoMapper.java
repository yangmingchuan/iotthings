package com.ymc.iotthings.dao;

import com.ymc.iotthings.model.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
 *
 * 参考博客：https://juejin.im/post/5ad6b3c3f265da237c696ba0
 * package name: com.vip.things.dao
 * date :2019年3月25日 08:53:59
 * author : ymc
 **/

public interface UserInfoMapper {

    UserInfo selectById(@Param("id") Integer id);
}
