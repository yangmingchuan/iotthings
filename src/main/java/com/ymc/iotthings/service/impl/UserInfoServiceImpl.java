package com.ymc.iotthings.service.impl;

import com.ymc.iotthings.dao.UserInfoMapper;
import com.ymc.iotthings.model.UserInfo;
import com.ymc.iotthings.service.UserInfoService;
import com.ymc.iotthings.core.universal.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: UserInfoService接口实现类
* @author ymc
* @date 2019/05/29 10:17
*/
@Service
public class UserInfoServiceImpl extends AbstractService<UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

}