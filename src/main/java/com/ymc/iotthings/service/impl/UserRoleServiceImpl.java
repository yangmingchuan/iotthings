package com.ymc.iotthings.service.impl;

import com.ymc.iotthings.core.universal.AbstractService;
import com.ymc.iotthings.dao.UserRoleMapper;
import com.ymc.iotthings.model.UserRole;
import com.ymc.iotthings.service.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: UserRoleService接口实现类
* @author ymc
* @date 2019/05/29 10:28
*/
@Service
public class UserRoleServiceImpl extends AbstractService<UserRole> implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 根绝用户id  获取角色
     * @param userId
     * @return
     */
    @Override
    public List<String> getRolesByUserId(String userId) {
        return userRoleMapper.getRolesByUserId(userId);
    }
}