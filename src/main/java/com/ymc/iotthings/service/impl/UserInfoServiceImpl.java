package com.ymc.iotthings.service.impl;

import com.ymc.iotthings.dao.UserInfoMapper;
import com.ymc.iotthings.model.UserInfo;
import com.ymc.iotthings.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 参考博客：https://juejin.im/post/5ad6b3c3f265da237c696ba0
 * package name: com.vip.things.service.impl
 * date :2019年3月25日 08:54:21
 * author : ymc
 **/

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    public UserInfo selectById(Integer id){
        return userInfoMapper.selectById(id);
    }

}
