package com.ymc.iotthings.service.impl;

import com.ymc.iotthings.core.universal.AbstractService;
import com.ymc.iotthings.dao.RolePermMapper;
import com.ymc.iotthings.model.RolePerm;
import com.ymc.iotthings.service.RolePermService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: RolePermService接口实现类
* @author ymc
* @date 2019/05/29 10:32
*/
@Service
public class RolePermServiceImpl extends AbstractService<RolePerm> implements RolePermService {

    @Resource
    private RolePermMapper rolePermMapper;

    /**
     * 根据 用户id 获取 权限
     * @param userId
     * @return
     */
    @Override
    public List<String> getPermsByUserId(String userId) {
        return rolePermMapper.getPermsByUserId(userId);
    }
}