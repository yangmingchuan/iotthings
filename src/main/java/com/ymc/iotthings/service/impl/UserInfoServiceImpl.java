package com.ymc.iotthings.service.impl;

import com.ymc.iotthings.core.configurer.ServiceException;
import com.ymc.iotthings.dao.UserInfoMapper;
import com.ymc.iotthings.model.UserInfo;
import com.ymc.iotthings.service.UserInfoService;
import com.ymc.iotthings.core.universal.AbstractService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 参考博客：https://juejin.im/post/5ad6b3c3f265da237c696ba0
 * package name: com.vip.things.service.impl
 * date :2019年3月25日 08:54:21
 * author : ymc
 **/

@Service
public class UserInfoServiceImpl extends AbstractService<UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo selectById(String id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if (userInfo == null) {
            throw new ServiceException("暂无该用户");
        }
        return userInfo;
    }
}

